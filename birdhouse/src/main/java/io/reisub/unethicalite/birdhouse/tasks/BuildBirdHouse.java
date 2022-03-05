package io.reisub.unethicalite.birdhouse.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.items.Inventory;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.TileObject;

public class BuildBirdHouse extends Task {
    @Override
    public String getStatus() {
        return "Building bird house";
    }

    @Override
    public boolean validate() {
        return Inventory.contains((i) -> Constants.BIRD_HOUSE_ITEM_IDS.contains(i.getId()));
    }

    @Override
    public void execute() {
        TileObject space = TileObjects.getNearest(
                (o) -> Constants.BIRD_HOUSE_SPACES.contains(o.getId())
                        && o.getTransformedComposition().getImpostor() != null
                        && o.getTransformedComposition().getImpostor().getId() == Constants.BIRD_HOUSE_EMPTY_SPACE
        );
        if (space == null) {
            return;
        }

        space.interact(0);
        Time.sleepTicksUntil(() -> !Inventory.contains((i) -> Constants.BIRD_HOUSE_ITEM_IDS.contains(i.getId())), 5);
    }
}
