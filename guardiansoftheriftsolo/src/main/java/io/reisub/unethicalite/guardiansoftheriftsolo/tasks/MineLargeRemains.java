package io.reisub.unethicalite.guardiansoftheriftsolo.tasks;

import io.reisub.unethicalite.guardiansoftheriftsolo.GuardiansOfTheRiftSolo;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.AreaType;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.PluginActivity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ObjectID;
import net.runelite.api.Player;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;

public class MineLargeRemains extends Task {

  private static final WorldPoint END_OF_RUBBLE = new WorldPoint(3637, 9503, 0);

  @Inject
  private GuardiansOfTheRiftSolo plugin;

  @Override
  public String getStatus() {
    return "Mining large remains";
  }

  @Override
  public boolean validate() {
    return AreaType.getCurrent() == AreaType.LARGE_REMAINS
        && !plugin.isCurrentActivity(PluginActivity.MINING)
        && (plugin.isGameActive() || Players.getLocal().getWorldLocation().equals(END_OF_RUBBLE));
  }

  @Override
  public void execute() {
    final TileObject remains = TileObjects.getNearest(ObjectID.LARGE_GUARDIAN_REMAINS);

    if (remains == null) {
      return;
    }

    remains.interact("Mine");

    final Player local = Players.getLocal();
    if (!Time.sleepTicksUntil(() -> local.isMoving() || local.isAnimating(), 3)) {
      return;
    }

    if (plugin.isGameActive()) {
      Time.sleepTicksUntil(() -> plugin.isCurrentActivity(PluginActivity.MINING), 15);
    }
  }
}
