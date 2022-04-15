package io.reisub.unethicalite.funguspicker.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;

public class Pick extends Task {
    @Override
    public String getStatus() {
        return "Picking fungus";
    }

    @Override
    public boolean validate() {
        return !Inventory.isFull()
                && TileObjects.getNearest(ObjectID.FUNGI_ON_LOG) != null;
    }

    @Override
    public void execute() {
        int count = Inventory.getCount(ItemID.MORT_MYRE_FUNGUS);

        TileObjects.getNearest(ObjectID.FUNGI_ON_LOG).interact("Pick");

        Time.sleepTicksUntil(() -> count < Inventory.getCount(ItemID.MORT_MYRE_FUNGUS), 5);
    }
}
