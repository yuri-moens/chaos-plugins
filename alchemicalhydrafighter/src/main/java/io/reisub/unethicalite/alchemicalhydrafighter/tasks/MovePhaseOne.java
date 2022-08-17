package io.reisub.unethicalite.alchemicalhydrafighter.tasks;

import io.reisub.unethicalite.alchemicalhydra.entity.Hydra;
import io.reisub.unethicalite.alchemicalhydra.entity.HydraPhase;
import io.reisub.unethicalite.alchemicalhydrafighter.AlchemicalHydraFighter;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.movement.Movement;

public class MovePhaseOne extends Task {

  @Inject
  private AlchemicalHydraFighter plugin;

  @Override
  public String getStatus() {
    return "Moving";
  }

  @Override
  public boolean validate() {
    final Hydra hydra = plugin.getHydraPlugin().getHydra();
    final WorldPoint vent = plugin.getRedVentLocation();

    if (plugin.getPhase() != HydraPhase.POISON
        || vent == null) {
      return false;
    }

    if (hydra.getNextSpecial() == 0) {
      return true;
    }

    return hydra.getNpc() != null
        && hydra.getNpc().distanceTo(vent) > 5;
  }

  @Override
  public void execute() {
    final WorldPoint vent = plugin.getRedVentLocation();
    final WorldPoint target = vent.dx(6).dy(-4);

    Movement.walk(target);

    final NPC hydra = plugin.getHydraPlugin().getHydra().getNpc();

    if (hydra == null) {
      return;
    }

    Time.sleepTicksUntil(() -> hydra.distanceTo(vent) < 5, 20);
  }
}
