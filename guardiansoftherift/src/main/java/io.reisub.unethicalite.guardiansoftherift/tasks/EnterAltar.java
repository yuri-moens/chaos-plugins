package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.guardiansoftherift.data.CellType;
import io.reisub.unethicalite.guardiansoftherift.data.GotrArea;
import io.reisub.unethicalite.guardiansoftherift.data.GuardianInfo;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.DynamicObject;
import net.runelite.api.GameObject;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.widgets.Dialog;

@Slf4j
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
        && (Inventory.isFull() || plugin.getGuardianPower() >= config.guardianPowerLastRun())
        && Inventory.contains("Guardian essence");
  }

  @Override
  public void execute() {
    GuardianInfo bestGuardian = plugin.getBestGuardian();
    if (bestGuardian != null) {
      if ((bestGuardian.getCellType() == CellType.WEAK
          || bestGuardian.getCellType() == CellType.MEDIUM)
          && plugin.getEntranceTimer() <= config.betterGuardianWaitTime()) {
        log.info("Waiting for better guardian");
        Movement.walk(new WorldPoint(3615, 9509, 0));

        Time.sleepTicksUntil(() -> plugin.getEntranceTimer() > config.betterGuardianWaitTime(), 30);
        return;
      }

      TileObject guardian = TileObjects.getNearest(bestGuardian.getObjectId());
      if (guardian != null) {
        guardian.interact("Enter");

        if (!Time.sleepTicksUntil(() -> Players.getLocal().isMoving(), 3)) {
          return;
        }

        Time.sleepTicksUntil(() -> GotrArea.getCurrent() == GotrArea.ALTAR
            || !bestGuardian.equals(plugin.getBestGuardian())
            || Dialog.isOpen(), 24);

        if (Dialog.isOpen()) {
          return;
        }

        if (plugin.getBestGuardian() == null || !bestGuardian.equals(plugin.getBestGuardian())) {
          Time.sleepTicksUntil(() -> GotrArea.getCurrent() == GotrArea.ALTAR, 2);
          Time.sleepTick();
        }
      }
    }
  }

  private boolean isActive(TileObject guardian) {
    return ((DynamicObject) ((GameObject) guardian).getRenderable()).getAnimationID() == 9363;
  }
}