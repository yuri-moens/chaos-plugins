package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.movement.Movement;


public class MoveToFirstCell extends Task {
  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Moving to first cell location";
  }

  @Override
  public boolean validate() {

    return plugin.getGamePhase() == 2
        &&
        TileObjects.getNearest("Inactive cell tile").distanceTo(Players.getLocal()) > 2;
  }

  @Override
  public int execute() {
    Movement.walk(new WorldPoint(3622, 9503, 0));
    Time.sleepTicksUntil(() -> Players.getLocal().isIdle(), 10);

    return 1;
  }
}