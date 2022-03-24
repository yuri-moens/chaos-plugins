package io.reisub.unethicalite.zmi.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.items.Inventory;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import io.reisub.unethicalite.zmi.Zmi;
import net.runelite.api.NullObjectID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;

public class RuneCraft extends Task {
    @Override
    public String getStatus() {
        return "Crafting runes";
    }

    @Override
    public boolean validate() {
        return Inventory.contains(Predicates.ids(Constants.ESSENCE_IDS))
                && Players.getLocal().isIdle()
                && !Players.getLocal().isMoving();
    }

    @Override
    public void execute() {
        if (!Inventory.isFull()) {
            Zmi.pouchesAreEmpty = true;
        }

        TileObject altar = TileObjects.getNearest(ObjectID.ALTAR_29631);
        if (altar == null) {
            return;
        }

        altar.interact("Craft-rune");
        Time.sleepTick();
    }
}
