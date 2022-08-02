package io.reisub.unethicalite.barrows.tasks;

import io.reisub.unethicalite.barrows.Barrows;
import io.reisub.unethicalite.barrows.Config;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ChaosBank;
import io.reisub.unethicalite.utils.tasks.BankTask;
import java.time.Duration;
import javax.inject.Inject;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.ItemID;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Dialog;

public class HandleBank extends BankTask {
  @Inject private Barrows plugin;

  @Inject private Config config;

  private boolean shouldRechargeStaff;
  private boolean ibanStaffAlmostEmpty;

  @Override
  public boolean validate() {
    return Utils.isInRegion(Barrows.FEROX_ENCLAVE_REGIONS)
        && isLastBankDurationAgo(Duration.ofSeconds(2))
        && isBankObjectAvailable()
        && (!Inventory.contains((i) -> Constants.PRAYER_RESTORE_POTION_IDS.contains(i.getId()))
            || (!Inventory.contains(Predicates.ids(Constants.DUELING_RING_IDS))
                && !Equipment.contains(Predicates.ids(Constants.DUELING_RING_IDS)))
            || Inventory.getCount(config.food()) < config.foodQuantity()
            || shouldRechargeStaff
            || ibanStaffAlmostEmpty
            || Inventory.contains(i -> i.getName().contains("Clue scroll"))
            || Inventory.contains(Predicates.ids(Constants.BARROWS_UNDEGRADED_IDS)));
  }

  @Override
  public int execute() {
    if (ibanStaffAlmostEmpty) {
      ibanStaffAlmostEmpty = false;
      plugin.stop("Iban staff is almost empty. Stopping plugin.");
    }

    if (!open(true)) {
      return 1;
    }

    if (shouldRechargeStaff) {
      shouldRechargeStaff = false;
      rechargeStaff();
    }

    Inventory.getAll(Predicates.ids(Constants.BARROWS_UNDEGRADED_IDS))
        .forEach(i -> Bank.depositAll(i.getId()));

    Inventory.getAll(i -> i.getName().contains("Clue scroll"))
        .forEach(i -> Bank.depositAll(i.getId()));

    if (!Inventory.contains((i) -> Constants.PRAYER_RESTORE_POTION_IDS.contains(i.getId()))) {
      if (Bank.contains(ItemID.PRAYER_POTION4)) {
        Bank.withdraw(ItemID.PRAYER_POTION4, 1, Bank.WithdrawMode.ITEM);
      } else if (Bank.contains(ItemID.PRAYER_POTION3)) {
        Bank.withdraw(ItemID.PRAYER_POTION3, 1, Bank.WithdrawMode.ITEM);
      } else if (Bank.contains(ItemID.SUPER_RESTORE4)) {
        Bank.withdraw(ItemID.SUPER_RESTORE4, 1, Bank.WithdrawMode.ITEM);
      } else if (Bank.contains(ItemID.SUPER_RESTORE3)) {
        Bank.withdraw(ItemID.SUPER_RESTORE3, 1, Bank.WithdrawMode.ITEM);
      } else {
        plugin.stop("No more 3 or 4 dose prayer or super restore potions. Stopping plugin.");
      }
    }

    int foodQuantity = config.foodQuantity() - Inventory.getCount(config.food());

    if (foodQuantity > 0) {
      if (Bank.getCount(true, config.food()) < foodQuantity) {
        plugin.stop("Out of food. Stopping plugin.");
      }

      Bank.withdraw(config.food(), foodQuantity, Bank.WithdrawMode.ITEM);
      Time.sleepTicksUntil(() -> Inventory.getCount(config.food()) >= config.foodQuantity(), 3);
    }

    if (!Inventory.contains(Predicates.ids(Constants.DUELING_RING_IDS))
        && !Equipment.contains(Predicates.ids(Constants.DUELING_RING_IDS))) {
      if (!Bank.contains(Predicates.ids(Constants.DUELING_RING_IDS))) {
        plugin.stop("Out of rings of dueling. Stopping plugin.");
      }

      Bank.withdraw(Predicates.ids(Constants.DUELING_RING_IDS), 1, Bank.WithdrawMode.ITEM);

      if (Equipment.fromSlot(EquipmentInventorySlot.RING) == null) {
        if (Time.sleepTicksUntil(
            () -> Inventory.contains(Predicates.ids(Constants.DUELING_RING_IDS)), 3)) {
          Inventory.getFirst(Predicates.ids(Constants.DUELING_RING_IDS)).interact("Wear");
        }
      }
    }

    return 1;
  }

  @Subscribe
  private void onChatMessage(ChatMessage event) {
    if (event.getMessage().contains("has 100 charges left")) {
      if (Equipment.contains(ItemID.TRIDENT_OF_THE_SEAS_E, ItemID.TRIDENT_OF_THE_SEAS)) {
        shouldRechargeStaff = true;
      } else if (Equipment.contains(
          ItemID.IBANS_STAFF, ItemID.IBANS_STAFF_U, ItemID.IBANS_STAFF_1410)) {
        ibanStaffAlmostEmpty = true;
      }
    }
  }

  private void rechargeStaff() {
    if (Inventory.getFreeSlots() < 5) {
      Bank.depositAll(config.food());
    }

    Bank.withdrawAll(ItemID.DEATH_RUNE, Bank.WithdrawMode.ITEM);
    Bank.withdrawAll(ItemID.CHAOS_RUNE, Bank.WithdrawMode.ITEM);
    Bank.withdrawAll(ItemID.FIRE_RUNE, Bank.WithdrawMode.ITEM);
    Bank.withdrawAll("Coins", Bank.WithdrawMode.ITEM);

    close();
    Time.sleepTicksUntil(() -> !Bank.isOpen(), 5);

    if (Inventory.getCount(true, ItemID.DEATH_RUNE) <= 100
        || Inventory.getCount(true, ItemID.CHAOS_RUNE) <= 100
        || Inventory.getCount(true, ItemID.FIRE_RUNE) <= 500
        || Inventory.getCount(true, "Coins") <= 1000) {
      plugin.stop("Not enough runes for 100 trident charges. Stopping plugin.");
    }

    Equipment.fromSlot(EquipmentInventorySlot.WEAPON).interact("Remove");
    Time.sleepTicksUntil(() -> Inventory.contains(Predicates.ids(Constants.TRIDENT_IDS)), 5);

    Inventory.getFirst(ItemID.DEATH_RUNE)
        .useOn(Inventory.getFirst(Predicates.ids(Constants.TRIDENT_IDS)));
    Time.sleepTicksUntil(Dialog::isEnterInputOpen, 3);

    Dialog.enterAmount(99999);
    Time.sleepTicksUntil(Dialog::canContinue, 10);

    Inventory.getFirst(Predicates.ids(Constants.TRIDENT_IDS)).interact("Wield");

    open();

    ChaosBank.depositAll(ItemID.DEATH_RUNE, ItemID.CHAOS_RUNE, ItemID.FIRE_RUNE);
    Bank.depositAll("Coins");
  }
}
