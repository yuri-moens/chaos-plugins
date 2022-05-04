package io.reisub.unethicalite.plankmaker.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.items.Bank;
import dev.unethicalite.api.items.Bank.WithdrawMode;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.client.Static;
import io.reisub.unethicalite.plankmaker.Config;
import io.reisub.unethicalite.plankmaker.PlankMaker;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.BankTask;
import javax.inject.Inject;
import net.runelite.api.GameState;

public class HandleBank extends BankTask {
  @Inject private Config config;

  @Override
  public boolean validate() {
    return !Inventory.contains(config.logType().getLogId())
        && Utils.isInRegion(PlankMaker.CAMELOT_REGION)
        && Static.getClient().getGameState() == GameState.LOGGED_IN;
  }

  @Override
  public void execute() {
    open(true);

    if (!Inventory.contains("Coins")) {
      Bank.withdrawAll("Coins", WithdrawMode.ITEM);
    }

    Bank.withdrawAll(config.logType().getLogId(), WithdrawMode.ITEM);
    Time.sleepTicksUntil(() -> Inventory.contains(config.logType().getLogId()), 3);
  }
}
