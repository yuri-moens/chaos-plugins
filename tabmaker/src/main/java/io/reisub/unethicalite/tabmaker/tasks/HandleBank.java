package io.reisub.unethicalite.tabmaker.tasks;

import com.google.common.collect.ImmutableSet;
import io.reisub.unethicalite.tabmaker.Config;
import io.reisub.unethicalite.tabmaker.TabMaker;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ChaosBank;
import io.reisub.unethicalite.utils.api.Interact;
import io.reisub.unethicalite.utils.tasks.BankTask;
import java.time.Duration;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Bank.WithdrawMode;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.client.Static;

public class HandleBank extends BankTask {

  @Inject
  private TabMaker plugin;
  @Inject
  private Config config;

  private static final Set<Integer> DONT_DEPOSIT;

  static {
    ImmutableSet.Builder<Integer> builder =  ImmutableSet.builder();

    DONT_DEPOSIT = builder
        .addAll(Constants.RUNE_IDS)
        .add(ItemID.RUNE_POUCH)
        .build();
  }

  private int tabsMade = 0;

  @Override
  public boolean validate() {
    return !Inventory.contains(ItemID.SOFT_CLAY)
        && isLastBankDurationAgo(Duration.ofSeconds(5));
  }

  @Override
  public void execute() {
    if (Static.getClient().isInInstancedRegion()) {
      Interact.interactWithInventoryOrEquipment(
          Constants.CRAFTING_CAPE_IDS,
          "Teleport",
          null,
          -1
      );

      Time.sleepTicksUntil(() -> Utils.isInRegion(Constants.CRAFTING_GUILD_REGION), 10);
      Time.sleepTicks(2);
    }

    tabsMade += Inventory.getCount(true, i -> i.hasAction("Break"));

    if (config.amount() > 0 && tabsMade >= config.amount()) {
      plugin.stop("Made " + tabsMade + " tabs as requested. Stopping plugin.");
      return;
    }

    open(true);

    ChaosBank.depositAllExcept(false, Predicates.ids(DONT_DEPOSIT));

    if (!Bank.contains(ItemID.SOFT_CLAY)) {
      plugin.stop("Out of clay. Stopping plugin.");
      return;
    }


    if (config.amount() > 0) {
      final int leftToMake = config.amount() - tabsMade;

      if (leftToMake >= Inventory.getFreeSlots()) {
        Bank.withdrawAll(ItemID.SOFT_CLAY, WithdrawMode.ITEM);
      } else {
        Bank.withdraw(ItemID.SOFT_CLAY, leftToMake, WithdrawMode.ITEM);
      }
    } else {
      Bank.withdrawAll(ItemID.SOFT_CLAY, WithdrawMode.ITEM);
    }
  }
}
