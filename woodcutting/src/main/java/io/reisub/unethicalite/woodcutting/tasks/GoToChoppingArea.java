package io.reisub.unethicalite.woodcutting.tasks;

import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import io.reisub.unethicalite.woodcutting.Config;
import javax.inject.Inject;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Inventory;

public class GoToChoppingArea extends Task {

  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Going to chopping area";
  }

  @Override
  public boolean validate() {
    return !Inventory.isFull()
        && Players.getLocal().distanceTo(config.location().getWoodcuttingAreaPoint())
        > config.location().getWoodcuttingAreaRadius();
  }

  @Override
  public void execute() {
    ChaosMovement.walkTo(config.location().getWoodcuttingAreaPoint(), 2);
  }
}
