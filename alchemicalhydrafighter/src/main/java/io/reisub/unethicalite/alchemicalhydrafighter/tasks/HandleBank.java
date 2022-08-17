package io.reisub.unethicalite.alchemicalhydrafighter.tasks;

import io.reisub.unethicalite.alchemicalhydrafighter.AlchemicalHydraFighter;
import io.reisub.unethicalite.utils.api.ChaosBank;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.tasks.BankTask;
import javax.inject.Inject;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.movement.pathfinder.model.BankLocation;
import net.unethicalite.client.Static;

public class HandleBank extends BankTask {

  @Inject
  private AlchemicalHydraFighter plugin;

  @Override
  public boolean validate() {
    return !Static.getClient().isInInstancedRegion()
        && (!ChaosBank.haveAllItemsInEquipment(plugin.getInventory())
        || !ChaosBank.haveAllItemsInInventory(plugin.getEquipment()));
  }

  @Override
  public void execute() {
    if (!isBankObjectAvailable()) {
      ChaosMovement.walkTo(BankLocation.getNearest().getArea().getCenter());
    }

    open(true);

    if (!Bank.isOpen()) {
      return;
    }

    Bank.depositInventory();

    if (!ChaosBank.haveAllItemsInInventory(plugin.getEquipment())) {
      ChaosBank.withdrawEquipment(plugin.getEquipment());
    }

    if (!ChaosBank.haveAllItemsInEquipment(plugin.getInventory())) {
      ChaosBank.withdrawItems(plugin.getInventory());
    }
  }
}
