package io.reisub.unethicalite.cooking.tasks;

import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileItems;
import dev.unethicalite.api.game.Game;
import dev.unethicalite.api.items.Bank;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.cooking.Config;
import io.reisub.unethicalite.cooking.Cooking;
import io.reisub.unethicalite.utils.tasks.BankTask;
import java.time.Duration;
import lombok.AllArgsConstructor;
import net.runelite.api.ItemID;

@AllArgsConstructor
public class HandleBank extends BankTask {
  private static final int ROGUES_DEN_REGION = 12109;

  private final Cooking plugin;
  private final Config config;

  @Override
  public boolean validate() {
    return !Inventory.contains(config.foodId())
        && (!config.sonicMode()
            || TileItems.getFirstAt(Players.getLocal().getWorldLocation(), config.foodId()) == null)
        && isLastBankDurationAgo(Duration.ofSeconds(5));
  }

  @Override
  public void execute() {
    if (Players.getLocal().getWorldLocation().getRegionID() == ROGUES_DEN_REGION) {
      open("Emerald Benedict");
    } else {
      open();
    }

    Bank.depositInventory();

    if (!Bank.contains(config.foodId())) {
      plugin.stop("No more raw food to cook, stopping plugin");
    }

    if (config.foodId() == ItemID.GIANT_SEAWEED) {
      Bank.withdraw(config.foodId(), 4, Bank.WithdrawMode.ITEM);
    } else {
      Bank.withdrawAll(config.foodId(), Bank.WithdrawMode.ITEM);
    }

    plugin.setLastBank(Game.getClient().getTickCount());
  }
}
