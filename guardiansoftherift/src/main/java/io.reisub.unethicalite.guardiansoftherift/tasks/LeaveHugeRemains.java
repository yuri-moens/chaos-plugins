package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.guardiansoftherift.data.GotrArea;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class LeaveHugeRemains extends Task {

  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Leaving huge remains";
  }

  @Override
  public boolean validate() {
    return GotrArea.getCurrent() == GotrArea.HUGE_REMAINS
        && Inventory.isFull();
  }

  @Override
  public void execute() {
    TileObjects.getNearest("Portal").interact("Enter");
    Time.sleepTicksUntil(() -> GotrArea.getCurrent() == GotrArea.MAIN, 20);
    Time.sleepTick();
  }
}