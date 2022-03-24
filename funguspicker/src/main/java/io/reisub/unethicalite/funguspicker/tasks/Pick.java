package io.reisub.unethicalite.funguspicker.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.items.Inventory;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ItemID;

public class Pick extends Task {
    @Override
    public String getStatus() {
        return "Picking fungus";
    }

    @Override
    public boolean validate() {
        return !Inventory.isFull()
                && TileObjects.getNearest(0) != null; // TODO
    }

    @Override
    public void execute() {
        int count = Inventory.getCount(ItemID.MORT_MYRE_FUNGUS);

        TileObjects.getNearest(0).interact(0); // TODO

        Time.sleepTicksUntil(() -> count < Inventory.getCount(ItemID.MORT_MYRE_FUNGUS), 5);
    }
}
