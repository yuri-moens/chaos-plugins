package io.reisub.unethicalite.runecrafting.tasks;

import io.reisub.unethicalite.runecrafting.Config;
import io.reisub.unethicalite.runecrafting.Runecrafting;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.BankTask;
import java.time.Duration;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Bank.WithdrawMode;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.magic.SpellBook;

public class HandleBank extends BankTask {

  @Inject
  private Runecrafting plugin;
  @Inject
  private Config config;

  @Override
  public boolean validate() {
    return plugin.isNearBank()
        && !Inventory.isFull()
        && isLastBankDurationAgo(Duration.ofSeconds(5));
  }

  @Override
  public void execute() {
    if (!open()) {
      return;
    }

    if (Inventory.contains(Predicates.ids(Constants.DEGRADED_ESSENCE_POUCH_IDS))
        && SpellBook.getCurrent() == SpellBook.LUNAR) {
      withdrawItemsForNpcContact();
      return;
    }

    Bank.depositAll(Predicates.ids(Constants.RUNE_IDS));
    Bank.depositAll(ItemID.RUNE_POUCH);

    if (config.useStamina()
        && isStaminaExpiring(Duration.ofSeconds(config.stamineTimeLeft()))
        && !Equipment.contains(Predicates.ids(Constants.RING_OF_ENDURANCE_IDS))) {
      drinkStamina();
    }

    while (!Inventory.isFull()) {
      if (Inventory.contains(Predicates.ids(Constants.DEGRADED_ESSENCE_POUCH_IDS))
          && SpellBook.getCurrent() == SpellBook.LUNAR) {
        withdrawItemsForNpcContact();
        return;
      }

      withdraw();

      if (!plugin.arePouchesFull()) {
        if (fillPouches()) {
          plugin.setEmptyPouches(0);
          plugin.setFullPouches(4);
        }
      }
    }
  }

  private void withdraw() {
    Bank.withdrawAll(Predicates.ids(Constants.ESSENCE_IDS), WithdrawMode.ITEM);
    Time.sleepTicksUntil(Inventory::isFull, 3);
  }

  private boolean fillPouches() {
    for (Item pouch : Bank.Inventory.getAll(Predicates.ids(Constants.ESSENCE_POUCH_IDS))) {
      pouch.interact("Fill");
    }

    Time.sleepTicksUntil(() -> Inventory.getFreeSlots() > 0 || plugin.arePouchesFull(), 3);

    return Inventory.contains(Predicates.ids(Constants.ESSENCE_IDS));
  }

  private void withdrawItemsForNpcContact() {
    if (!Bank.contains(ItemID.AIR_RUNE)
        || !Bank.contains(ItemID.COSMIC_RUNE)
        || !Bank.contains(ItemID.ASTRAL_RUNE)) {
      Bank.withdraw(ItemID.RUNE_POUCH, 1, WithdrawMode.ITEM);
    }

    Bank.withdraw(ItemID.AIR_RUNE, 2, WithdrawMode.ITEM);
    Bank.withdraw(ItemID.COSMIC_RUNE, 1, WithdrawMode.ITEM);
    Bank.withdraw(ItemID.ASTRAL_RUNE, 1, WithdrawMode.ITEM);
    Bank.close();

    Time.sleepTicksUntil(() -> !Bank.isOpen(), 5);
  }
}
