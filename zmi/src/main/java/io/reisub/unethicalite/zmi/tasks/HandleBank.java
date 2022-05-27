package io.reisub.unethicalite.zmi.tasks;

import io.reisub.unethicalite.utils.api.ChaosBank;
import io.reisub.unethicalite.utils.tasks.BankTask;
import io.reisub.unethicalite.zmi.Config;
import io.reisub.unethicalite.zmi.Pouch;
import io.reisub.unethicalite.zmi.Zmi;
import java.time.Duration;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Inventory;

public class HandleBank extends BankTask {
  private static final int UNDERGROUND_REGION_ID = 12119;
  @Inject private Config config;

  @Override
  public boolean validate() {
    return Players.getLocal().getWorldLocation().getRegionID() == UNDERGROUND_REGION_ID
        && !Inventory.contains(ItemID.PURE_ESSENCE, ItemID.DAEYALT_ESSENCE)
        && isLastBankDurationAgo(Duration.ofSeconds(5))
        && Players.getLocal().distanceTo(Zmi.NEAR_ALTAR) > 10;
  }

  @Override
  public void execute() {
    Time.sleepTick();

    if (!open("Eniola")) {
      return;
    }

    ChaosBank.depositAllExcept(
        false,
        ItemID.GIANT_POUCH,
        ItemID.GIANT_POUCH_5515,
        ItemID.LARGE_POUCH,
        ItemID.LARGE_POUCH_5513,
        ItemID.MEDIUM_POUCH,
        ItemID.MEDIUM_POUCH_5511,
        ItemID.SMALL_POUCH,
        ItemID.RUNE_POUCH,
        ItemID.MIND_RUNE);

    if (config.useStamina() && isStaminaExpiring(Duration.ofSeconds(25))) {
      drinkStamina();
    }

    int essenceId =
        Bank.contains(ItemID.DAEYALT_ESSENCE) ? ItemID.DAEYALT_ESSENCE : ItemID.PURE_ESSENCE;

    Bank.withdrawAll(essenceId, Bank.WithdrawMode.ITEM);
    Time.sleepTicksUntil(Inventory::isFull, 3);

    Item giantPouch = Bank.Inventory.getFirst(ItemID.GIANT_POUCH);
    if (giantPouch != null) {
      Zmi.pouchesAreEmpty = false;

      ChaosBank.bankInventoryInteract(giantPouch, "Fill");
      Pouch.GIANT.addHolding(Pouch.GIANT.getHoldAmount());

      Bank.withdrawAll(essenceId, Bank.WithdrawMode.ITEM);
      Time.sleepTicksUntil(Inventory::isFull, 3);
    }

    List<Item> pouches =
        Bank.Inventory.getAll(ItemID.SMALL_POUCH, ItemID.MEDIUM_POUCH, ItemID.LARGE_POUCH);
    if (!pouches.isEmpty()) {
      Zmi.pouchesAreEmpty = false;

      for (Item pouch : pouches) {
        ChaosBank.bankInventoryInteract(pouch, "Fill");

        Pouch pouchEnum = Pouch.forItem(pouch.getId());
        if (pouchEnum != null) {
          pouchEnum.addHolding(pouchEnum.getHoldAmount());
        }
      }

      Bank.withdrawAll(essenceId, Bank.WithdrawMode.ITEM);
    }
  }
}
