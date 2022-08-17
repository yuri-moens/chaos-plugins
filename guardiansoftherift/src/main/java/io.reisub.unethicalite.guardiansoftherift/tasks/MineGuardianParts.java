package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.guardiansoftherift.data.GotrArea;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class MineGuardianParts extends Task {

  private final WorldPoint endOfRubble = new WorldPoint(3637, 9503, 0);
  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Mining guardian parts";
  }

  @Override
  public boolean validate() {
    return GotrArea.getCurrent() == GotrArea.MAIN
        && !Inventory.contains("Guardian fragments");
  }

  @Override
  public void execute() {
    TileObjects.getNearest("Guardian parts").interact("Mine");
    Time.sleepTicksUntil(
        () -> plugin.isPortalActive()
            || Inventory.getCount("Guardian fragments") >= Inventory.getFreeSlots() + 1, 50);
  }
}