package io.reisub.unethicalite.tempoross.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.packets.TileObjectPackets;
import io.reisub.unethicalite.tempoross.Tempoross;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.NullObjectID;
import net.runelite.api.TileObject;

import javax.inject.Inject;

public class Tether extends Task {
    @Inject
    private Tempoross plugin;

    @Override
    public String getStatus() {
        return "Tethering";
    }

    @Override
    public boolean validate() {
        if (!plugin.isInTemporossArea()) return false;

        return plugin.isWaveIncoming()
                && plugin.getCurrentActivity() != Activity.TETHERING_MAST
                && plugin.getCurrentActivity() != Activity.REPAIRING;
    }

    @Override
    public void execute() {
        TileObject tetherObject = TileObjects.getNearest(NullObjectID.NULL_41352, NullObjectID.NULL_41353, NullObjectID.NULL_41354, NullObjectID.NULL_41355);
        if (tetherObject == null) return;

        if (plugin.getCurrentActivity() != Activity.IDLE && plugin.getPreviousActivity() != Activity.REPAIRING) {
            int waitTicks = 10 - (Players.getLocal().distanceTo(tetherObject) / 2);
            Time.sleepTicksUntil(() -> plugin.getCurrentActivity() == Activity.IDLE, waitTicks);
        }

        TileObjectPackets.tileObjectFirstOption(tetherObject, false);

        Time.sleepUntil(() -> plugin.getCurrentActivity() == Activity.TETHERING_MAST, 10000);
        Time.sleepUntil(() -> plugin.getCurrentActivity() == Activity.IDLE, 20000);
    }
}
