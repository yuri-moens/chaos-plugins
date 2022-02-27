package io.reisub.unethicalite.tempoross.tasks;

import dev.hoot.api.commons.Rand;
import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.packets.MovementPackets;
import dev.hoot.api.packets.TileObjectPackets;
import dev.hoot.api.widgets.Dialog;
import io.reisub.unethicalite.tempoross.Tempoross;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;

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
                && (plugin.getPlayersReady() == 0 || Dialog.canContinueNPC());
    }

    @Override
    public void execute() {
        TileObject ladder = TileObjects.getNearest(ObjectID.ROPE_LADDER_41305);
        if (ladder == null) {
            return;
        }

        TileObjectPackets.tileObjectFirstOption(ladder, false);
        Time.sleepTicks(Rand.nextInt(3, 5));
        Time.sleepUntil(() -> plugin.isOnBoat() || plugin.getPlayersReady() >= 1, 10000);

        if (!plugin.isOnBoat() && plugin.getPlayersReady() >= 1) {
            MovementPackets.sendMovement(new WorldPoint(3142, 2839, 0));
            Time.sleep(400, 700);
        }
    }
}
