package io.reisub.unethicalite.alchemicalhydrafighter.tasks;

import io.reisub.unethicalite.alchemicalhydra.entity.HydraPhase;
import io.reisub.unethicalite.alchemicalhydrafighter.AlchemicalHydraFighter;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Projectile;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.client.Static;

public class DodgePoison extends Task {

  @Inject
  private AlchemicalHydraFighter plugin;

  @Override
  public String getStatus() {
    return "Dodging poison";
  }

  @Override
  public boolean validate() {
    return (plugin.getPhase() == HydraPhase.POISON || plugin.getPhase() == HydraPhase.ENRAGED)
        && !plugin.getHydraPlugin().getPoisonProjectiles().isEmpty();
  }

  @Override
  public void execute() {
    final WorldPoint target = getNearestSafeTileAdjacentToHydra();

    if (target == null) {
      return;
    }

    Movement.walk(target);
    Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().equals(target), 10);
  }

  private List<WorldPoint> getPoisonPoints() {
    final Map<LocalPoint, Projectile> poisonMap = plugin.getHydraPlugin().getPoisonProjectiles();
    final List<WorldPoint> poisonPoints = new ArrayList<>();

    for (LocalPoint target : poisonMap.keySet()) {
      final WorldPoint sw = WorldPoint.fromLocalInstance(Static.getClient(), target).dx(-1).dx(-1);

      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          poisonPoints.add(sw.dx(i).dy(j));
        }
      }
    }

    return poisonPoints;
  }

  private List<WorldPoint> getAdjacentPoints() {
    final NPC hydra = plugin.getHydraPlugin().getHydra().getNpc();

    if (hydra == null) {
      return null;
    }

    final WorldPoint sw = hydra.getWorldLocation();

    final List<WorldPoint> adjacentPoints = new ArrayList<>();

    for (int i = 0; i < 6; i++) {
      adjacentPoints.add(sw.dx(-1).dy(i));
      adjacentPoints.add(sw.dx(6).dy(i));
      adjacentPoints.add(sw.dx(i).dy(-1));
      adjacentPoints.add(sw.dx(i).dy(6));
    }

    return adjacentPoints;
  }

  private WorldPoint getNearestSafeTileAdjacentToHydra() {
    final List<WorldPoint> poisonPoints = getPoisonPoints();
    final List<WorldPoint> safePoints = getAdjacentPoints();

    if (safePoints == null) {
      return null;
    }

    safePoints.removeAll(poisonPoints);

    WorldPoint nearest = null;

    for (WorldPoint safe : safePoints) {
      if (nearest == null) {
        nearest = safe;
        continue;
      }

      final Player local = Players.getLocal();

      if (local.distanceTo(safe) < local.distanceTo(nearest)) {
        nearest = safe;
      }
    }

    return nearest;
  }
}
