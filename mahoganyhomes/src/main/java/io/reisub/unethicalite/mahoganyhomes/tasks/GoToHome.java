package io.reisub.unethicalite.mahoganyhomes.tasks;

import io.reisub.unethicalite.mahoganyhomes.Config;
import io.reisub.unethicalite.mahoganyhomes.MahoganyHomes;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.client.Static;

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
  public int execute() {
    plugin.teleport();

    if (!Utils.isInRegion(plugin.getCurrentHome().getRegions())) {
      return 1;
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

    return 1;
  }
}
