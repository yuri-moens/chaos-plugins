package io.reisub.unethicalite.tempoross.tasks;

import dev.hoot.api.commons.Rand;
import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.TileObjects;
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
        if (plugin.getCurrentActivity() != Activity.IDLE && plugin.getPreviousActivity() != Activity.REPAIRING) {
            Time.sleepUntil(() -> plugin.getCurrentActivity() == Activity.IDLE, Rand.nextInt(2200, 2500));
        }

        TileObject tetherObject = TileObjects.getNearest(NullObjectID.NULL_41352, NullObjectID.NULL_41353, NullObjectID.NULL_41354, NullObjectID.NULL_41355);
        if (tetherObject == null) return;

        tetherObject.interact(0);

        Time.sleepUntil(() -> plugin.getCurrentActivity() == Activity.TETHERING_MAST, 10000);
        Time.sleepUntil(() -> plugin.getCurrentActivity() == Activity.IDLE, 20000);
    }
}
