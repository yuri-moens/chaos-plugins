package io.reisub.unethicalite.farming.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.game.Vars;
import dev.hoot.api.items.Inventory;
import dev.hoot.bot.managers.Static;
import io.reisub.unethicalite.farming.Farming;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.TileObject;

import javax.inject.Inject;

public class Pick extends Task {
    @Inject
    private Farming plugin;

    @Override
    public String getStatus() {
        return "Picking herbs";
    }

    @Override
    public boolean validate() {
        TileObject patch = TileObjects.getNearest(Predicates.ids(Constants.HERB_PATCH_IDS));

        return patch != null && patch.hasAction("Pick");
    }

    @Override
    public void execute() {
        TileObject patch = TileObjects.getNearest(Predicates.ids(Constants.HERB_PATCH_IDS));
        if (patch == null) {
            return;
        }

        patch.interact("Pick");
        Time.sleepTicksUntil(() -> Players.getLocal().isAnimating(), 20);

        int current = Static.getClient().getTickCount();

        while (Static.getClient().getTickCount() <= current + 3) {
            patch.interact("Pick");
            Time.sleep(150, 220);
        }

        Time.sleepTicksUntil(() -> Inventory.isFull() || Vars.getBit(plugin.getCurrentLocation().getVarbit()) <= 3, 100);
    }
}
