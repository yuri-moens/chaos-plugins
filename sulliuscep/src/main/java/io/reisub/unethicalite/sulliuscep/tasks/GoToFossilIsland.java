package io.reisub.unethicalite.sulliuscep.tasks;

import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.enums.HouseTeleport;
import io.reisub.unethicalite.utils.tasks.Task;
import net.unethicalite.client.Static;

public class GoToFossilIsland extends Task {

  @Override
  public String getStatus() {
    return "Going to Fossil Island";
  }

  @Override
  public boolean validate() {
    return Utils.isInRegion(Constants.CRAFTING_GUILD_REGION)
        || Static.getClient().isInInstancedRegion();
  }

  @Override
  public void execute() {
    ChaosMovement.teleportThroughHouse(HouseTeleport.FOSSIL_ISLAND, 101);
  }
}
