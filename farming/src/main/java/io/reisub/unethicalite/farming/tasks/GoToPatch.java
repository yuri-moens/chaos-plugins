package io.reisub.unethicalite.farming.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.magic.Magic;
import dev.hoot.api.magic.Regular;
import io.reisub.unethicalite.farming.Config;
import io.reisub.unethicalite.farming.Farming;
import io.reisub.unethicalite.utils.api.CMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;

import javax.inject.Inject;

public abstract class GoToPatch extends Task {
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
        WorldPoint current = Players.getLocal().getWorldLocation();

        plugin.getCurrentLocation().getTeleportable().teleport();
        Time.sleepTicksUntil(() -> !Players.getLocal().getWorldLocation().equals(current), 10);

        CMovement.walkTo(plugin.getCurrentLocation().getPatchPoint());
    }
}
