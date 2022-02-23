package io.reisub.unethicalite.tempoross.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.TileObjects;
import io.reisub.unethicalite.tempoross.Tempoross;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;

import javax.inject.Inject;

public class LeaveBoat extends Task {
    @Inject
    private Tempoross plugin;

    @Override
    public String getStatus() {
        return "Leaving boat";
    }

    @Override
    public boolean validate() {
        return plugin.isOnBoat()
                && plugin.getPlayersReady() > 1;
    }

    @Override
    public void execute() {
        TileObject ladder = TileObjects.getNearest(ObjectID.ROPE_LADDER_41305);
        if (ladder == null) return;

        ladder.interact("Quick-climb");
        Time.sleepUntil(() -> plugin.isInDesert(), 10000);
    }
}
