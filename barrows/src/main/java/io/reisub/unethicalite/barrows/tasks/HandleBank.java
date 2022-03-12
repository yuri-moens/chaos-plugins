package io.reisub.unethicalite.barrows.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.items.Bank;
import dev.hoot.api.items.Equipment;
import dev.hoot.api.items.Inventory;
import io.reisub.unethicalite.barrows.Barrows;
import io.reisub.unethicalite.barrows.Config;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.CBank;
import io.reisub.unethicalite.utils.tasks.BankTask;
import net.runelite.api.ItemID;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import java.time.Duration;

public class HandleBank extends BankTask {
    @Inject
    private Barrows plugin;

    @Inject
    private Config config;

    private boolean shouldRechargeStaff;
    private boolean ibanStaffAlmostEmpty;

    @Override
    public boolean validate() {
        return plugin.isInFeroxEnclave()
                && isLastBankDurationAgo(Duration.ofSeconds(5))
                && (
                        !Inventory.contains((i) -> Constants.PRAYER_RESTORE_POTION_IDS.contains(i.getId()))
                                || Inventory.getCount(config.food()) < config.foodQuantity()
                                || shouldRechargeStaff
                                || ibanStaffAlmostEmpty
                );
    }

    @Override
    public void execute() {
        if (ibanStaffAlmostEmpty) {
            ibanStaffAlmostEmpty = false;
            plugin.stop("Iban staff is almost empty. Stopping plugin.");
        }

        open();

        if (shouldRechargeStaff) {
            shouldRechargeStaff = false;
            rechargeStaff();
        }

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

            if (foodQuantity > 4) {
                Bank.withdraw(config.food(), foodQuantity, Bank.WithdrawMode.ITEM);
            } else {
                for (int i = 0; i < foodQuantity; i++) {
                    Bank.withdraw(config.food(), 1, Bank.WithdrawMode.ITEM);
                }
            }
        }
    }

    @Subscribe
    private void onChatMessage(ChatMessage event) {
        if (event.getMessage().contains("Your staff has 100 charges left")) {
            if (Equipment.contains(ItemID.TRIDENT_OF_THE_SEAS_E, ItemID.TRIDENT_OF_THE_SEAS)) {
                shouldRechargeStaff = true;
            } else if (Equipment.contains(ItemID.IBANS_STAFF, ItemID.IBANS_STAFF_U, ItemID.IBANS_STAFF_1410)) {
                ibanStaffAlmostEmpty = true;
            }
        }
    }

    private void rechargeStaff() {
        if (Bank.getCount(true, ItemID.DEATH_RUNE) <= 100
                || Bank.getCount(true, ItemID.CHAOS_RUNE) <= 100
                || Bank.getCount(true, ItemID.FIRE_RUNE) <= 500
                || Bank.getCount(true, "Coins") <= 1000) {
            plugin.stop("Not enough runes for 100 trident charges. Stopping plugin.");
        }

        if (Inventory.getFreeSlots() < 5) {
            Bank.depositAll(config.food());
        }

        Bank.withdrawAll(ItemID.DEATH_RUNE, Bank.WithdrawMode.ITEM);
        Bank.withdrawAll(ItemID.CHAOS_RUNE, Bank.WithdrawMode.ITEM);
        Bank.withdrawAll(ItemID.FIRE_RUNE, Bank.WithdrawMode.ITEM);
        Bank.withdrawAll("Coins", Bank.WithdrawMode.ITEM);

        close();
        Time.sleepTicksUntil(() -> !Bank.isOpen(), 5);

        // TODO unequip staff if necessary and charge it, then re-equip

        open();

        CBank.depositAll(ItemID.DEATH_RUNE, ItemID.CHAOS_RUNE, ItemID.FIRE_RUNE);
        Bank.depositAll("Coins");
    }
}
