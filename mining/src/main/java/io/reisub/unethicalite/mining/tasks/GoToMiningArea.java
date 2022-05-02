package io.reisub.unethicalite.mining.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.movement.Movement;
import io.reisub.unethicalite.mining.Config;
import io.reisub.unethicalite.mining.Mining;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;

public class GoToMiningArea extends Task {
  @Inject private Mining plugin;

  @Inject private Config config;

  @Override
  public String getStatus() {
    return "Going to mining area";
  }

  @Override
  public boolean validate() {
    return Players.getLocal().distanceTo(config.location().getMiningAreaPoint())
            > config.location().getMiningAreaDistance()
        && !Inventory.isFull();
  }

  @Override
  public void execute() {
    if (!Movement.isRunEnabled()) {
      if (!config.location().isThreeTick()
          || (config.location().isThreeTick() && Movement.getRunEnergy() > 70)) {
        Movement.toggleRun();
      }
    }

    if (config.location().isThreeTick()) {
      ChaosMovement.walkTo(config.location().getMiningAreaPoint());

      Time.sleepTicksUntil(
          () ->
              Players.getLocal().getWorldLocation().equals(config.location().getMiningAreaPoint()),
          20);
      plugin.setArrived(true);
    } else {
      ChaosMovement.walkTo(config.location().getMiningAreaPoint(), 2);
    }
  }
}
