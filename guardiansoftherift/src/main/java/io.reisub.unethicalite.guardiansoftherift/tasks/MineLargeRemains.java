package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class MineLargeRemains extends Task {
  private final WorldPoint endOfRubble = new WorldPoint(3637, 9503, 0);
  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Mining large remains";
  }

  @Override
  public boolean validate() {
    return plugin.getGamePhase() == 4 && Players.getLocal().getWorldLocation().equals(endOfRubble);
  }

  @Override
  public int execute() {
    TileObjects.getNearest("Large guardian remains").interact("Mine");
    Time.sleepTicksUntil(() -> Inventory.getCount(true, "Guardian fragments") >= 160, 200);
    plugin.setGamePhase(5);

    return 1;
  }
}