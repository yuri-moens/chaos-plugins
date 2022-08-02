package io.reisub.unethicalite.woodcutting.tasks;

import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import io.reisub.unethicalite.woodcutting.Config;
import io.reisub.unethicalite.woodcutting.Woodcutting;
import javax.inject.Inject;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.client.Static;

public class GoToChoppingArea extends Task {

  @Inject
  private Woodcutting plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Going to chopping area";
  }

  @Override
  public boolean validate() {
    return
        (!Inventory.isFull() || Static.getClient().getTickCount() - plugin.getLastBankTick() <= 2)
            && Players.getLocal().distanceTo(config.location().getWoodcuttingAreaPoint())
            > config.location().getWoodcuttingAreaRadius();
  }

  @Override
  public int execute() {
    ChaosMovement.walkTo(config.location().getWoodcuttingAreaPoint(), 2);
    return 1;
  }
}
