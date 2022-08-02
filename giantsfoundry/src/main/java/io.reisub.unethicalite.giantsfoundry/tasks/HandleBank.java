package io.reisub.unethicalite.giantsfoundry.tasks;

import io.reisub.unethicalite.giantsfoundry.Config;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundry;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryHelper;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryState;
import io.reisub.unethicalite.giantsfoundry.enums.Alloy;
import io.reisub.unethicalite.utils.tasks.BankTask;
import java.util.Map;
import javax.inject.Inject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Bank.WithdrawMode;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Dialog;
import net.unethicalite.client.Static;

public class HandleBank extends BankTask {

  @Inject
  GiantsFoundryState giantsFoundryState;
  @Inject
  GiantsFoundryHelper giantsFoundryHelper;
  @Inject
  private GiantsFoundry plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Banking";
  }

  @Override
  public boolean validate() {
    return (giantsFoundryState.getGameStage() == 1
        || Static.getClient().getTickCount() - plugin.getLastSetMoulds() < 3)
        && giantsFoundryState.getOreCount() == 0
        && needIngredients();
  }

  @Override
  public int execute() {
    open(15, 10);
    Bank.depositInventory();

    final Map<String, Integer> ingredients = plugin.getIngredients();

    if (ingredients == null) {
      Alloy alloy1 = config.alloy1();
      Alloy alloy2 = config.alloy2();

      if (alloy1.equals(alloy2)) {
        Bank.withdrawAll(alloy1.getBarId(), Bank.WithdrawMode.ITEM);
      } else {
        Bank.withdraw(alloy1.getBarId(), 14, Bank.WithdrawMode.ITEM);
        Bank.withdraw(alloy2.getBarId(), 14, Bank.WithdrawMode.ITEM);
      }
    } else {
      for (Map.Entry<String, Integer> ingredient : ingredients.entrySet()) {
        Bank.withdraw(ingredient.getKey(), ingredient.getValue(), WithdrawMode.ITEM);
      }

      Time.sleepTick();
    }

    if (Dialog.isOpen()) {
      Dialog.close();
    }

    close();

    return 1;
  }

  private boolean needIngredients() {
    final Map<String, Integer> ingredients = plugin.getIngredients();

    if (ingredients == null) {
      return !Inventory.contains(config.alloy1().getBarId(), config.alloy2().getBarId());
    } else {
      return !Inventory.contains(Predicates.names(ingredients.keySet()));
    }
  }
}