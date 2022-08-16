package io.reisub.unethicalite.guardiansoftheriftsolo.tasks;

import io.reisub.unethicalite.guardiansoftheriftsolo.GuardiansOfTheRiftSolo;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.AreaType;
import io.reisub.unethicalite.utils.api.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class LeaveHugeRemains extends Task {

  @Inject
  private GuardiansOfTheRiftSolo plugin;

  @Override
  public String getStatus() {
    return "Leaving huge remains";
  }

  @Override
  public boolean validate() {
    return AreaType.getCurrent() == AreaType.HUGE_REMAINS
        && Inventory.isFull();
  }

  @Override
  public void execute() {
    plugin.setActivity(Activity.IDLE);

    final TileObject portal = TileObjects.getNearest("Portal");

    if (portal == null) {
      return;
    }

    portal.interact("Enter");
    Time.sleepTicksUntil(() -> AreaType.getCurrent() == AreaType.MAIN, 20);
  }
}
