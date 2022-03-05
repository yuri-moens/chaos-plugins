package io.reisub.unethicalite.birdhouse.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.items.Inventory;
import io.reisub.unethicalite.birdhouse.BirdHouse;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;

@RequiredArgsConstructor
public class EmptyBirdHouse extends Task {
    private final BirdHouse plugin;

    private TileObject birdHouse;

    @Override
    public String getStatus() {
        return "Emptying bird house";
    }

    @Override
    public boolean validate() {
        birdHouse = TileObjects.getNearest(
                (o) -> Constants.BIRD_HOUSE_SPACES.contains(o.getId())
                        && !plugin.isEmptied(o.getId())
                        && o.getTransformedComposition().getImpostor() != null
                        && Constants.BIRD_HOUSE_IDS.contains(o.getTransformedComposition().getImpostor().getId())
        );

        return birdHouse != null;
    }

    @Override
    public void execute() {
        birdHouse.interact(2);
        if (!Time.sleepTicksUntil(() -> Inventory.contains(ItemID.CLOCKWORK), 15)) {
            return;
        }

        plugin.emptied(birdHouse.getId());
    }
}
