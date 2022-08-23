package io.reisub.unethicalite.runecrafting.tasks;

import io.reisub.unethicalite.runecrafting.Config;
import io.reisub.unethicalite.runecrafting.Runecrafting;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.api.Interact;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Inventory;

public class GoToBank extends Task {

  @Inject
  private Runecrafting plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Going to bank";
  }

  @Override
  public boolean validate() {
    return !Inventory.contains(Predicates.ids(Constants.ESSENCE_IDS))
        && !plugin.isNearBank();
  }

  @Override
  public void execute() {
    if (config.rune().getBankPoint() == null) {
      switch (config.bankLocation()) {
        case CRAFTING_GUILD:
          Interact.interactWithInventoryOrEquipment(
              Constants.CRAFTING_CAPE_IDS,
              "Teleport",
              null,
              -1
          );

          Time.sleepTicksUntil(() -> Utils.isInRegion(Constants.CRAFTING_GUILD_REGION), 10);
          Time.sleepTick();
          break;
        default:
      }
    } else {
      ChaosMovement.walkTo(config.rune().getBankPoint());
    }
  }
}
