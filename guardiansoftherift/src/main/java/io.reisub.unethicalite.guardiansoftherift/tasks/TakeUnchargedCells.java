package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.guardiansoftherift.data.GotrArea;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class TakeUnchargedCells extends Task {

  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Taking uncharged cells";
  }

  @Override
  public boolean validate() {
    if (GotrArea.getCurrent() != GotrArea.MAIN) {
      return false;
    }

    if (Inventory.getCount(true, "Uncharged cell") >= 8) {
      return false;
    }

    // grab when game is not running
    if (plugin.getElapsedTicks() == -1) {
      return true;
    }

    // grab at start of game when we started at portal
    if (plugin.getElapsedTicks() < 100 && Inventory.contains(ItemID.GUARDIAN_ESSENCE)) {
      return true;
    }

    // grab near end of game
    return plugin.getGuardianPower() > config.guardianPowerLastRun()
        && (plugin.getPortalTimer() == -1 || plugin.getPortalTimer() > 10)
        && !Inventory.contains(ItemID.GUARDIAN_ESSENCE)
        && !Inventory.contains(Predicates.ids(Constants.RUNE_IDS));
  }

  @Override
  public void execute() {
    if (Inventory.isFull() && !Inventory.contains("Uncharged cell")) {
      Inventory.getFirst(ItemID.GUARDIAN_ESSENCE).drop();;
    }

    TileObjects.getNearest("Uncharged cells").interact("Take-10");
    Time.sleepTicksUntil(() -> Inventory.getCount(true, "Uncharged cell") == 10, 20);
  }
}