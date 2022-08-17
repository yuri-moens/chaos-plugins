package io.reisub.unethicalite.guardiansoftheriftsolo.tasks;

import io.reisub.unethicalite.guardiansoftheriftsolo.data.AreaType;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ObjectID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;

public class EnterPortal extends Task {

  @Override
  public String getStatus() {
    return "Entering portal";
  }

  @Override
  public boolean validate() {
    return AreaType.getCurrent() == AreaType.MAIN
        && TileObjects.getNearest(ObjectID.PORTAL_43729) != null;
  }

  @Override
  public void execute() {
    TileObjects.getNearest(ObjectID.PORTAL_43729).interact("Enter");
    Time.sleepTicksUntil(() -> AreaType.getCurrent() == AreaType.HUGE_REMAINS, 20);
  }
}
