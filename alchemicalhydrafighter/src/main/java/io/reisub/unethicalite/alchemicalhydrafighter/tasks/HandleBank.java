package io.reisub.unethicalite.alchemicalhydrafighter.tasks;

import io.reisub.unethicalite.alchemicalhydrafighter.AlchemicalHydraFighter;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ChaosBank;
import io.reisub.unethicalite.utils.tasks.BankTask;
import javax.inject.Inject;
import net.unethicalite.api.items.Bank;
import net.unethicalite.client.Static;

public class HandleBank extends BankTask {

  private static final int MOUNTAIN_REGION = 0;

  @Inject
  private AlchemicalHydraFighter plugin;

  @Override
  public boolean validate() {
    return !Utils.isInRegion(MOUNTAIN_REGION)
        && !Static.getClient().isInInstancedRegion();
  }

  @Override
  public int execute() {
    open(true);

    if (!Bank.isOpen()) {
      return;
    }

    Bank.depositInventory();

    ChaosBank.withdrawEquipment(plugin.getEquipment());

    ChaosBank.withdrawItems(plugin.getInventory());

  }
}
