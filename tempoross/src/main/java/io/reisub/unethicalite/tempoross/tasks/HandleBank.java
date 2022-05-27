package io.reisub.unethicalite.tempoross.tasks;

import io.reisub.unethicalite.tempoross.Tempoross;
import io.reisub.unethicalite.utils.tasks.BankTask;
import java.time.Duration;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;

public class HandleBank extends BankTask {
  @Inject private Tempoross plugin;

  private int bucketCount;

  @Override
  public boolean validate() {
    if (!plugin.isInDesert()) {
      return false;
    }

    bucketCount = Inventory.getCount(ItemID.BUCKET, ItemID.BUCKET_OF_WATER);

    return isLastBankDurationAgo(Duration.ofSeconds(5))
        && (bucketCount < 4
            || (!Inventory.contains(ItemID.HAMMER, ItemID.IMCANDO_HAMMER)
                && !Equipment.contains(ItemID.IMCANDO_HAMMER)));
  }

  @Override
  public void execute() {
    if (!open()) {
      return;
    }

    if (bucketCount < 4) {
      Bank.withdraw(ItemID.BUCKET, 4 - bucketCount, Bank.WithdrawMode.ITEM);
    }

    if (!Inventory.contains(ItemID.HAMMER, ItemID.IMCANDO_HAMMER)) {
      if (Bank.contains(ItemID.IMCANDO_HAMMER)) {
        Bank.withdraw(ItemID.IMCANDO_HAMMER, 1, Bank.WithdrawMode.ITEM);
      } else {
        Bank.withdraw(ItemID.HAMMER, 1, Bank.WithdrawMode.ITEM);
      }
    }

    Bank.close();
  }
}
