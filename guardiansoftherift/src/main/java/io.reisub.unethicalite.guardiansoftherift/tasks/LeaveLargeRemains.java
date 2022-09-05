package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.guardiansoftherift.data.GotrArea;
import io.reisub.unethicalite.utils.api.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class LeaveLargeRemains extends Task {

  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Leaving large remains";
  }

  @Override
  public boolean validate() {
    if (GotrArea.getCurrent() != GotrArea.LARGE_REMAINS) {
      return false;
    }

    final int elapsedTicks = plugin.getElapsedTicks();

    if (Inventory.contains(ItemID.GUARDIAN_ESSENCE)) {
      return elapsedTicks >= config.ticksBeforeLeavingRemainsPortalStart();
    }

    return elapsedTicks >= config.ticksBeforeLeavingRemains();
  }

  @Override
  public void execute() {
    plugin.setActivity(Activity.IDLE);

    TileObjects.getNearest("Rubble").interact("Climb");
    Time.sleepTicksUntil(() -> GotrArea.getCurrent() == GotrArea.MAIN, 10);
    Time.sleepTicks(3);
  }
}