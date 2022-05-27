package io.reisub.unethicalite.enchanter.tasks;

import com.google.common.collect.ImmutableSet;
import io.reisub.unethicalite.enchanter.Config;
import io.reisub.unethicalite.enchanter.EnchantItem;
import io.reisub.unethicalite.enchanter.Enchanter;
import io.reisub.unethicalite.utils.api.ChaosBank;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.BankTask;
import java.time.Duration;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Inventory;

public class HandleBank extends BankTask {
  @Inject private Enchanter plugin;

  @Inject private Config config;

  @Override
  public boolean validate() {
    if (!isLastBankDurationAgo(Duration.ofSeconds(2))) {
      return false;
    }

    if (config.item() == EnchantItem.ALL) {
      return !Inventory.contains(Predicates.ids(EnchantItem.getAllItemsFor(config.spell())));
    } else {
      return !Inventory.contains(config.item().getId());
    }
  }

  @Override
  public void execute() {
    open();

    ChaosBank.depositAllExcept(
        false,
        ItemID.RUNE_POUCH,
        ItemID.RUNE_POUCH_23650,
        ItemID.AIR_RUNE,
        ItemID.WATER_RUNE,
        ItemID.EARTH_RUNE,
        ItemID.FIRE_RUNE,
        ItemID.BLOOD_RUNE,
        ItemID.SOUL_RUNE,
        ItemID.COSMIC_RUNE);

    Set<Integer> ids;

    if (config.item() == EnchantItem.ALL) {
      ids = EnchantItem.getAllItemsFor(config.spell());
    } else {
      ids = ImmutableSet.of(config.item().getId());
    }

    if (!Bank.contains(Predicates.ids(ids))) {
      plugin.stop("No more items to enchant. Stopping plugin.");
      return;
    }

    Bank.withdrawAll(Predicates.ids(ids), Bank.WithdrawMode.ITEM);
  }
}
