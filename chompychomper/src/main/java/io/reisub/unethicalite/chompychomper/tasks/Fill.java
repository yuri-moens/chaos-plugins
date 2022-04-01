package io.reisub.unethicalite.chompychomper.tasks;

import com.google.common.collect.ImmutableSet;
import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.game.GameThread;
import dev.hoot.api.items.Inventory;
import io.reisub.unethicalite.chompychomper.ChompyChomper;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

public class Fill extends Task {
    @Inject
    private ChompyChomper plugin;

    private final Set<Integer> SWAMP_BUBBLES_IDS = ImmutableSet.of(
            ObjectID.SWAMP_BUBBLES,
            ObjectID.SWAMP_BUBBLES_735,
            ObjectID.SWAMP_BUBBLES_30667,
            ObjectID.SWAMP_BUBBLES_33640
    );

    @Override
    public String getStatus() {
        return "Filling bellows";
    }

    @Override
    public boolean validate() {
        return plugin.getCurrentActivity() == Activity.IDLE
                && Inventory.contains(ItemID.OGRE_BELLOWS)
                && !Inventory.contains(Predicates.ids(ChompyChomper.FILLED_BELLOW_IDS));
    }

    @Override
    public void execute() {
        TileObject bubbles = TileObjects.getNearest(Predicates.ids(SWAMP_BUBBLES_IDS));
        if (bubbles == null) {
            return;
        }

        List<Item> bellows = Inventory.getAll(ItemID.OGRE_BELLOWS);

        GameThread.invoke(() -> bellows.get(0).useOn(bubbles));
        Time.sleepTicksUntil(() -> Players.getLocal().isAnimating(), 20);
        Time.sleepTicks(1);

        bellows.subList(1, bellows.size()).forEach((i) -> {
            GameThread.invoke(() -> i.useOn(bubbles));
            Time.sleepTicks(1);
        });
    }
}
