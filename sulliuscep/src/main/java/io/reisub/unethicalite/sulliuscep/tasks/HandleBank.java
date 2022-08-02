package io.reisub.unethicalite.sulliuscep.tasks;

import io.reisub.unethicalite.sulliuscep.Config;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ChaosBank;
import io.reisub.unethicalite.utils.tasks.BankTask;
import java.time.Duration;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Bank.WithdrawMode;
import net.unethicalite.api.items.Inventory;

public class HandleBank extends BankTask {

  @Inject
  private Config config;

  @Override
  public boolean validate() {
    return Utils.isInRegion(Constants.CRAFTING_GUILD_REGION)
        && Inventory.contains(Predicates.nameContains("Unidentified"))
        && isLastBankDurationAgo(Duration.ofSeconds(5));
  }

  @Override
  public int execute() {
    open(true);

    ChaosBank.depositAll(false, Predicates.nameContains("Unidentified"));
    ChaosBank.depositAll(
        false,
        ItemID.SULLIUSCEP_CAP,
        ItemID.MUSHROOM,
        ItemID.MORT_MYRE_FUNGUS,
        ItemID.SARADOMIN_BREW1,
        ItemID.SARADOMIN_BREW2,
        ItemID.SARADOMIN_BREW3
    );

    final int brewsToWithdraw = config.brewsToWithdraw()
        - Inventory.getCount(ItemID.SARADOMIN_BREW4);

    if (brewsToWithdraw > 0) {
      Bank.withdraw(ItemID.SARADOMIN_BREW4, brewsToWithdraw, WithdrawMode.ITEM);
    }

    return 1;
  }
}
