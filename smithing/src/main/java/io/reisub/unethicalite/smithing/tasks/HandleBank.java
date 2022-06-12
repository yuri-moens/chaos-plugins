package io.reisub.unethicalite.smithing.tasks;

import io.reisub.unethicalite.smithing.Config;
import io.reisub.unethicalite.smithing.Smithing;
import io.reisub.unethicalite.utils.api.ChaosBank;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.BankTask;
import java.time.Duration;
import lombok.AllArgsConstructor;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Bank.WithdrawMode;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Dialog;

@AllArgsConstructor
public class HandleBank extends BankTask {

  private final Smithing plugin;
  private final Config config;

  @Override
  public boolean validate() {
    return plugin.getCurrentActivity() == Activity.IDLE
        && isLastBankDurationAgo(Duration.ofSeconds(3))
        && Inventory.getCount(config.metal().getBarId()) < config.product().getRequiredBars();
  }

  @Override
  public void execute() {
    plugin.setActivity(Activity.BANKING);

    if (!open()) {
      plugin.setActivity(Activity.IDLE);
      return;
    }

    if (!Inventory.contains(ItemID.HAMMER, ItemID.IMCANDO_HAMMER)
        && !Equipment.contains(ItemID.IMCANDO_HAMMER)) {
      if (Bank.contains(ItemID.IMCANDO_HAMMER)) {
        Bank.withdraw(ItemID.IMCANDO_HAMMER, 1, Bank.WithdrawMode.ITEM);
      } else {
        Bank.withdraw(ItemID.HAMMER, 1, Bank.WithdrawMode.ITEM);
      }
    }

    ChaosBank.depositAllExcept(
        false, ItemID.HAMMER, ItemID.IMCANDO_HAMMER, config.metal().getBarId());

    if (config.amount() > 0 && plugin.getItemsMade() >= config.amount()) {
      plugin.stop("Made " + config.amount() + " items. Stopping plugin.");
    }

    final int emptySlots = Inventory.contains(ItemID.HAMMER, ItemID.IMCANDO_HAMMER) ? 27 : 28;

    if (Bank.getCount(true, config.metal().getBarId()) >= config.product().getRequiredBars()) {
      if (config.amount() > 0
          && (config.amount() - plugin.getItemsMade()) * config.product().getRequiredBars()
          < emptySlots) {
        Bank.withdraw(config.metal().getBarId(),
            (config.amount() - plugin.getItemsMade()) * config.product().getRequiredBars(),
            WithdrawMode.ITEM);

        Time.sleepTick();

        if (Dialog.isOpen()) {
          Dialog.close();
        }
      } else {
        Bank.withdrawAll(config.metal().getBarId(), Bank.WithdrawMode.ITEM);
      }

      plugin.setActivity(Activity.IDLE);
    } else {
      plugin.stop("Out of bars. Stopping plugin.");
    }
  }
}
