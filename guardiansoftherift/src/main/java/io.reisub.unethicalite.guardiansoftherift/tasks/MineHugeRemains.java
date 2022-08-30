package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.guardiansoftherift.data.GotrArea;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class MineHugeRemains extends Task {

  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Mining huge remains";
  }

  @Override
  public boolean validate() {
    return !Inventory.isFull()
        && GotrArea.getCurrent() == GotrArea.HUGE_REMAINS
        && plugin.getElapsedTicks() != -1
        && plugin.getGuardianPower() != -1
        && plugin.getGuardianPower() < config.guardianPowerLastRun();
  }

  @Override
  public void execute() {
    while (!Inventory.isFull()
        && plugin.getElapsedTicks() != -1) {
      mine();

      if (!plugin.arePouchesFull()) {
        if (plugin.fillPouches()) {
          plugin.setEmptyPouches(0);
          plugin.setFullPouches(4);
        }
      }
    }
  }

  private void mine() {
    final TileObject remains = TileObjects.getNearest("Huge guardian remains");

    if (remains == null) {
      return;
    }

    remains.interact("Mine");

    if (!Time.sleepTicksUntil(() -> Players.getLocal().isMoving()
        || Players.getLocal().isAnimating(), 3)) {
      return;
    }

    if (plugin.arePouchesFull()) {
      Time.sleepTicksUntil(Inventory::isFull, 20);
    } else {
      Time.sleepTicksUntil(() -> Inventory.getFreeSlots() < 4, 20);
    }
  }
}