package io.reisub.unethicalite.secondarygatherer.tasks;

import io.reisub.unethicalite.secondarygatherer.Config;
import io.reisub.unethicalite.secondarygatherer.Secondary;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.BankTask;
import java.time.Duration;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Bank.WithdrawMode;
import net.unethicalite.api.items.Inventory;

public class HandleBank extends BankTask {

  private static final int CRAFTING_GUILD_REGION = 11571;

  @Inject
  private Config config;

  @Override
  public boolean validate() {
    return config.secondary() == Secondary.BLUE_DRAGON_SCALE
        && isLastBankDurationAgo(Duration.ofSeconds(5))
        && Inventory.contains(ItemID.BLUE_DRAGON_SCALE)
        && Utils.isInRegion(CRAFTING_GUILD_REGION);
  }

  @Override
  public void execute() {
    open(true);

    Bank.depositAll(ItemID.BLUE_DRAGON_SCALE);

    if (Combat.getMissingHealth() > 25) {
      Bank.withdraw(config.food(), 2, WithdrawMode.ITEM);
    }
  }
}
