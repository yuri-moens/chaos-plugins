package io.reisub.unethicalite.mining.tasks;

import io.reisub.unethicalite.mining.Config;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class GoToBank extends Task {

  private static final int TRAHAEARN_MINE_REGION = 13250;
  private static final int SALT_MINE_REGION = 11425;

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
      case BASALT:
        final TileObject steps2 = TileObjects.getNearest(ObjectID.STEPS_33261);

        if (steps2 == null) {
          return;
        }

        steps2.interact("Climb");

        Time.sleepTicksUntil(() -> !Utils.isInRegion(SALT_MINE_REGION), 30);
        Time.sleepTick();
        break;
      default:
        ChaosMovement.walkTo(config.location().getBankPoint());
        break;
    }
  }
}
