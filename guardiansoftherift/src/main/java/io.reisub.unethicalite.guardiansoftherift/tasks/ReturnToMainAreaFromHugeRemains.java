package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;


public class ReturnToMainAreaFromHugeRemains extends Task {
  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Returning to the game";
  }

  @Override
  public boolean validate() {
    return Players.getLocal().getWorldLocation().getWorldX() < 3597 && Inventory.isFull();
  }

  @Override
  public int execute() {
    TileObjects.getNearest("Portal").interact("Enter");
    Time.sleepTicksUntil(plugin::checkInMainRegion, 20);

    return 2;
  }
}