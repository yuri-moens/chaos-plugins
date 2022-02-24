package io.reisub.unethicalite.tempoross.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.packets.TileObjectPackets;
import io.reisub.unethicalite.tempoross.Tempoross;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.NullObjectID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;

import javax.inject.Inject;

public class Repair extends Task {
    @Inject
    private Tempoross plugin;

    TileObject brokenObject;

    @Override
    public String getStatus() {
        return "Repairing";
    }

    @Override
    public boolean validate() {
        if (!plugin.isInTemporossArea() || !plugin.isWaveIncoming() || plugin.getCurrentActivity() == Activity.REPAIRING) return false;

        TileObject tetherObject = TileObjects.getNearest(NullObjectID.NULL_41352, NullObjectID.NULL_41353, NullObjectID.NULL_41354, NullObjectID.NULL_41355);
        brokenObject = TileObjects.getNearest(ObjectID.DAMAGED_MAST_40996, ObjectID.DAMAGED_MAST_40997, ObjectID.DAMAGED_TOTEM_POLE, ObjectID.DAMAGED_TOTEM_POLE_41011);

        if (brokenObject == null) return false;

        if (tetherObject == null) return true;

        return Players.getLocal().distanceTo(tetherObject) > Players.getLocal().distanceTo(brokenObject);
    }

    @Override
    public void execute() {
        TileObjectPackets.tileObjectFirstOption(brokenObject, false);
        Time.sleepUntil(() -> plugin.getCurrentActivity() == Activity.REPAIRING, 15000);
    }
}
