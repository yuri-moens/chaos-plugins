package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;


public class ReturnToMainArea extends Task {
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
    return TileObjects.getNearest("Altar") != null && !Inventory.contains("Guardian essence");
  }

  @Override
  public void execute() {
    TileObjects.getNearest("Portal").interact("Use");
    Time.sleepTicksUntil(plugin::checkInMainRegion, 20);
    Time.sleepTicks(3);

  }
}