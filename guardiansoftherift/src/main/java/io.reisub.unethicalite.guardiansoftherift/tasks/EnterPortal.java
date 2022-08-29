package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.guardiansoftherift.data.GotrArea;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class EnterPortal extends Task {

  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Entering portal";
  }

  @Override
  public boolean validate() {
    return !Inventory.isFull()
        && TileObjects.getNearest(43729) != null
        && GotrArea.getCurrent() == GotrArea.MAIN;
  }

  @Override
  public void execute() {
    TileObjects.getNearest(43729).interact("Enter");
    Time.sleepTicksUntil(() -> GotrArea.getCurrent() == GotrArea.HUGE_REMAINS
        || TileObjects.getNearest(43729) == null, 20);
    Time.sleepTick();
  }
}