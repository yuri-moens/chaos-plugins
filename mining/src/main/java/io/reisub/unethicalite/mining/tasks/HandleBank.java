package io.reisub.unethicalite.mining.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.items.Bank;
import dev.unethicalite.api.items.Bank.WithdrawMode;
import dev.unethicalite.api.items.Equipment;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.mining.Config;
import io.reisub.unethicalite.mining.Location;
import io.reisub.unethicalite.utils.api.ChaosBank;
import io.reisub.unethicalite.utils.tasks.BankTask;
import java.time.Duration;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;

public class HandleBank extends BankTask {

  @Inject
  private Config config;

  @Override
  public boolean validate() {
    return Inventory.isFull()
        && config.location().getBankPoint() != null
        && !config.drop()
        && Players.getLocal().distanceTo(config.location().getBankPoint()) < 10
        && isLastBankDurationAgo(Duration.ofSeconds(5));
  }

  @Override
  public void execute() {
    open(true);

    final Item gemBag = Bank.Inventory.getFirst(ItemID.OPEN_GEM_BAG);

    if (gemBag != null) {
      ChaosBank.bankInventoryInteract(gemBag, "Empty");
      Time.sleepTick();
    }

    ChaosBank.depositAllExcept(ItemID.OPEN_GEM_BAG);

    if (config.location() == Location.SOFT_CLAY
        && !Equipment.contains(ItemID.BRACELET_OF_CLAY)
        && Bank.contains(ItemID.BRACELET_OF_CLAY)) {
      Time.sleepTick();
      Bank.withdraw(ItemID.BRACELET_OF_CLAY, 1, WithdrawMode.ITEM);
    }
  }
}
