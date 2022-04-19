package io.reisub.unethicalite.farming.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import io.reisub.unethicalite.farming.Config;
import io.reisub.unethicalite.farming.Farming;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.coords.WorldPoint;

public class GoToPatch extends Task {
  @Inject private Farming plugin;

  @Inject private Config config;

  @Override
  public String getStatus() {
    return "Going to " + plugin.getCurrentLocation().getName() + " patch";
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

    if (plugin.getCurrentLocation().getTeleportable().teleport()) {
      Time.sleepTicksUntil(
          () ->
              Players.getLocal().getWorldLocation() != null
                  && !Players.getLocal().getWorldLocation().equals(current),
          10);
    }

    if (Players.getLocal().distanceTo(plugin.getCurrentLocation().getPatchPoint()) > 10) {
      ChaosMovement.walkTo(plugin.getCurrentLocation().getPatchPoint());
    }
  }
}
