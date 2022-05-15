package io.reisub.unethicalite.mahoganyhomes.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.movement.Movement;
import dev.unethicalite.client.Static;
import io.reisub.unethicalite.mahoganyhomes.Config;
import io.reisub.unethicalite.mahoganyhomes.MahoganyHomes;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;

public class GoToHome extends Task {

  @Inject
  private MahoganyHomes plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Going to home";
  }

  @Override
  public boolean validate() {
    return plugin.getCurrentHome() != null
        && !plugin.getCurrentHome().isInHome(Players.getLocal());
  }

  @Override
  public void execute() {
    plugin.teleport();

    if (!Utils.isInRegion(plugin.getCurrentHome().getRegions())) {
      return;
    }

    if (Players.getLocal().getWorldLocation().getPlane() > 0) {
      plugin.useStairs();
    }

    if (plugin.getCurrentHome().getLocation().isInScene(Static.getClient())) {
      Movement.walk(plugin.getCurrentHome().getLocation());

      if (!Time.sleepTicksUntil(() -> Players.getLocal().isMoving(), 3)) {
        ChaosMovement.walkTo(plugin.getCurrentHome().getLocation(), 0, null, 100, 2);
      }
    } else {
      ChaosMovement.walkTo(plugin.getCurrentHome().getLocation(), 0, null, 100, 2);
    }

    Time.sleepTicksUntil(() -> plugin.getCurrentHome().isInHome(Players.getLocal()), 30);
  }
}
