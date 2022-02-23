package io.reisub.unethicalite.tempoross.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.TileObjects;
import io.reisub.unethicalite.tempoross.Tempoross;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;

import javax.inject.Inject;

public class EnterBoat extends Task {
    @Inject
    private Tempoross plugin;

    @Override
    public String getStatus() {
        return "Entering boat";
    }

    @Override
    public boolean validate() {
        return plugin.isInDesert()
                && plugin.getPlayersReady() == 0;
    }

    @Override
    public void execute() {
        TileObject ladder = TileObjects.getNearest(ObjectID.ROPE_LADDER_41305);
        if (ladder == null) return;

        ladder.interact("Climb");
        Time.sleepUntil(() -> plugin.isOnBoat(), 10000);
    }
}
