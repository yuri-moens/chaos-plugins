package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.guardiansoftherift.data.GotrArea;
import io.reisub.unethicalite.guardiansoftherift.data.GuardianInfo;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class EnterAltar extends Task {

  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Entering altar";
  }

  @Override
  public boolean validate() {
    return GotrArea.getCurrent() == GotrArea.MAIN
        && Inventory.isFull()
        && Inventory.contains("Guardian essence");
  }

  @Override
  public void execute() {
    GuardianInfo bestGuardian = plugin.getBestGuardian();
    if (bestGuardian != null) {
      TileObject guardian = TileObjects.getNearest(bestGuardian.getObjectId());
      if (guardian != null) {
        guardian.interact("Enter");

        if (!Time.sleepTicksUntil(() -> Players.getLocal().isMoving(), 3)) {
          return;
        }

        Time.sleepTicksUntil(() -> GotrArea.getCurrent() == GotrArea.ALTAR
            || !plugin.getBestGuardian().equals(bestGuardian), 24);
        Time.sleepTick();
      }
    }
  }
}