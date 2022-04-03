package io.reisub.unethicalite.zmi.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.widgets.Prayers;
import io.reisub.unethicalite.utils.tasks.Task;
import io.reisub.unethicalite.zmi.Config;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;

import javax.inject.Inject;

public class GoToBank extends Task {
    @Inject
    private Config config;

    private static final int OVERWORLD_REGION_ID = 9778;

    @Override
    public String getStatus() {
        return "Going to bank";
    }

    @Override
    public boolean validate() {
        return Players.getLocal().getWorldLocation().getRegionID() == OVERWORLD_REGION_ID;
    }

    @Override
    public void execute() {
        if (config.usePrayer()) {
            if (Prayers.getPoints() < 40) {
                TileObject altar = TileObjects.getNearest(ObjectID.CHAOS_ALTAR_411);
                if (altar == null) {
                    return;
                }

                altar.interact("Pray-at");
                Time.sleepTicksUntil(() -> Prayers.getPoints() > 40, 20);
            }
        }

        TileObject ladder = TileObjects.getNearest(ObjectID.LADDER_29635);
        if (ladder == null) {
            return;
        }

        ladder.interact("Climb");
        Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().getRegionID() != OVERWORLD_REGION_ID, 20);
    }
}
