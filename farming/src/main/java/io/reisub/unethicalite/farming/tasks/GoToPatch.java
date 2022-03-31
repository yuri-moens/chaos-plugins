package io.reisub.unethicalite.farming.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import io.reisub.unethicalite.farming.Config;
import io.reisub.unethicalite.farming.Farming;
import io.reisub.unethicalite.farming.Location;
import io.reisub.unethicalite.utils.api.CMovement;
import io.reisub.unethicalite.utils.tasks.Task;

import javax.inject.Inject;

public class GoToPatch extends Task {
    @Inject
    private Farming plugin;

    @Inject
    private Config config;

    @Override
    public String getStatus() {
        return "Going to " + plugin.getCurrentLocation().name() + " patch";
    }

    @Override
    public boolean validate() {
        return plugin.getCurrentLocation() != null
                && !plugin.getCurrentLocation().isDone()
                && Players.getLocal().distanceTo(plugin.getCurrentLocation().getPatchPoint()) > 10;
    }

    @Override
    public void execute() {
        if (plugin.getCurrentLocation().getTeleportable().teleport()) {
            if (plugin.getCurrentLocation() == Location.HOSIDIUS) {
                Time.sleepTicks(2);
            }
            Time.sleepTick();
        }

        CMovement.walkTo(plugin.getCurrentLocation().getPatchPoint());
    }
}
