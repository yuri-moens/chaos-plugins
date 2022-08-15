package io.reisub.unethicalite.alchemicalhydrafighter.tasks;

import io.reisub.unethicalite.alchemicalhydra.entity.HydraPhase;
import io.reisub.unethicalite.alchemicalhydrafighter.AlchemicalHydraFighter;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.entities.Players;

public class DodgeFire extends Task {

  @Inject
  private AlchemicalHydraFighter plugin;

  @Override
  public String getStatus() {
    return "Dodging fire";
  }

  @Override
  public boolean validate() {
    return plugin.getPhase() == HydraPhase.FLAME
        && plugin.getHydraPlugin().getHydra().getNextSpecial() == 0;
  }

  @Override
  public void execute() {
    final WorldPoint vent = plugin.getBlueVentLocation();
    final WorldPoint top = vent.dx(2).dy(-1);
    final WorldPoint bottom = vent.dx(1).dy(-2);

    if (Players.getLocal().getWorldLocation().equals(top)) {
      plugin.walk(bottom);
    } else {
      plugin.walk(top);
    }
  }
}
