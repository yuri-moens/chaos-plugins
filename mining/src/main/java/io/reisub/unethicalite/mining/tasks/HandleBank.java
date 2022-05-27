package io.reisub.unethicalite.mining.tasks;

import io.reisub.unethicalite.mining.Config;
import io.reisub.unethicalite.mining.Location;
import io.reisub.unethicalite.utils.api.ChaosBank;
import io.reisub.unethicalite.utils.tasks.BankTask;
import java.time.Duration;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Bank.WithdrawMode;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;

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
    if (config.location() == Location.BASALT) {
      final Item basalt = Inventory.getFirst(ItemID.BASALT);
      final NPC snowflake = NPCs.getNearest(NpcID.SNOWFLAKE);

      if (basalt == null || snowflake == null) {
        return;
      }

      GameThread.invoke(() -> basalt.useOn(snowflake));

      Time.sleepTicksUntil(() -> !Inventory.contains(ItemID.BASALT), 20);
      return;
    }

    open(true);

    final Item gemBag = Bank.Inventory.getFirst(ItemID.OPEN_GEM_BAG);

    if (gemBag != null) {
      ChaosBank.bankInventoryInteract(gemBag, "Empty");
      Time.sleepTick();
    }

    ChaosBank.depositAllExcept(false, ItemID.OPEN_GEM_BAG);

    if (config.location() == Location.SOFT_CLAY
        && !Equipment.contains(ItemID.BRACELET_OF_CLAY)
        && Bank.contains(ItemID.BRACELET_OF_CLAY)) {
      Time.sleepTick();
      Bank.withdraw(ItemID.BRACELET_OF_CLAY, 1, WithdrawMode.ITEM);
    }
  }
}
