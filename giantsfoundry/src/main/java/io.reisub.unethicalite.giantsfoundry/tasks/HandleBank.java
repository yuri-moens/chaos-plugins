package io.reisub.unethicalite.giantsfoundry.tasks;

import io.reisub.unethicalite.giantsfoundry.Config;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundry;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryHelper;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryState;
import io.reisub.unethicalite.giantsfoundry.enums.Alloy;
import io.reisub.unethicalite.utils.tasks.BankTask;
import javax.inject.Inject;
import net.unethicalite.api.items.Inventory;

public class Bank extends BankTask {
  @Inject
  GiantsFoundryState giantsFoundryState;
  @Inject
  GiantsFoundryHelper giantsFoundryHelper;
  @Inject
  private GiantsFoundry plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Banking";
  }

  @Override
  public boolean validate() {
    return !Inventory.isFull() && giantsFoundryState.getGameStage() == 1
        &&
        giantsFoundryState.getOreCount() == 0;

  }

  @Override
  public void execute() {
    open(15, 10);
    net.unethicalite.api.items.Bank.depositInventory();

    Alloy alloy1 = config.alloy1();
    Alloy alloy2 = config.alloy2();

    if (alloy1.equals(alloy2)) {
      net.unethicalite.api.items.Bank.withdrawAll(alloy1.getBarId(),
          net.unethicalite.api.items.Bank.WithdrawMode.ITEM);
    } else {
      net.unethicalite.api.items.Bank.withdraw(alloy1.getBarId(), 14,
          net.unethicalite.api.items.Bank.WithdrawMode.ITEM);
      net.unethicalite.api.items.Bank.withdraw(alloy2.getBarId(), 14,
          net.unethicalite.api.items.Bank.WithdrawMode.ITEM);
    }

    close();
  }
}