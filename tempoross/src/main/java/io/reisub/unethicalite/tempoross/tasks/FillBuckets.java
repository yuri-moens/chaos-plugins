package io.reisub.unethicalite.tempoross.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.NPCs;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.items.Inventory;
import io.reisub.unethicalite.tempoross.Tempoross;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ItemID;
import net.runelite.api.NpcID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;

import javax.inject.Inject;

public class FillBuckets extends Task {
    @Inject
    private Tempoross plugin;

    @Override
    public String getStatus() {
        return "Filling buckets";
    }

    @Override
    public boolean validate() {
        if (!Inventory.contains(ItemID.BUCKET)) return false;

        if (plugin.getCurrentActivity() == Activity.FILLING_BUCKETS) return false;

        if (plugin.isOnBoat()) return true;

        int filledBuckets = Inventory.getCount(ItemID.BUCKET_OF_WATER);
        int fires = NPCs.getAll((n) -> n.getId() == NpcID.FIRE_8643
                && (plugin.getIslandArea().contains(n) || plugin.getBoatArea().contains(n))).size();

        return plugin.isInTemporossArea() && filledBuckets < fires;
    }

    @Override
    public void execute() {
        TileObject pump = TileObjects.getNearest(ObjectID.WATER_PUMP_41000, ObjectID.WATER_PUMP_41004);
        if (pump == null) return;

        pump.interact(0);

        if (!Time.sleepUntil(() -> plugin.getCurrentActivity() == Activity.FILLING_BUCKETS, 15000)) return;
        Time.sleepUntil(() -> plugin.getCurrentActivity() == Activity.IDLE, 5000);
    }
}
