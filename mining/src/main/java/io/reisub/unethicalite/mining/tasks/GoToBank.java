package io.reisub.unethicalite.mining.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.mining.Config;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;

public class GoToBank extends Task {

  private static final int TRAHAEARN_MINE_REGION = 13250;

  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Going to bank";
  }

  @Override
  public boolean validate() {
    return Inventory.isFull()
        && config.location().getBankPoint() != null
        && !config.drop()
        && Players.getLocal().distanceTo(config.location().getBankPoint()) >= 10;
  }

  @Override
  public void execute() {
    switch (config.location()) {
      case SOFT_CLAY:
        final TileObject steps = TileObjects.getNearest(ObjectID.STEPS_36215);

        if (steps == null) {
          return;
        }

        steps.interact("Exit");

        Time.sleepTicksUntil(() -> !Utils.isInRegion(TRAHAEARN_MINE_REGION), 30);
        Time.sleepTick();

        ChaosMovement.walkTo(config.location().getBankPoint());
        break;
      default:
        ChaosMovement.walkTo(config.location().getBankPoint());
        break;
    }
  }
}
