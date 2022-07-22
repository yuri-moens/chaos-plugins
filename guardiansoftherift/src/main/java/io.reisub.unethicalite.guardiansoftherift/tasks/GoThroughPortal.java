package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardianInfo;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;


public class GoThroughPortal extends Task {
  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Entering PORTAL";
  }

  @Override
  public boolean validate() {
    return plugin.getGamePhase() == 10 && !Inventory.isFull() && TileObjects.getNearest(43729) != null;
  }

  @Override
  public void execute() {
    TileObjects.getNearest(43729).interact("Enter");
    Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().getWorldX() < 3597, 10);
    Time.sleepTick();
  }
}