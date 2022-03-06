package io.reisub.unethicalite.smithing.tasks;

import dev.hoot.api.items.Bank;
import dev.hoot.api.items.Equipment;
import dev.hoot.api.items.Inventory;
import io.reisub.unethicalite.smithing.Config;
import io.reisub.unethicalite.smithing.Smithing;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.BankTask;
import lombok.AllArgsConstructor;
import net.runelite.api.ItemID;

import java.time.Duration;

@AllArgsConstructor
public class HandleBank extends BankTask {
    private final Smithing plugin;
    private final Config config;

    @Override
    public boolean validate() {
        return plugin.getCurrentActivity() == Activity.IDLE
                && isLastBankDurationAgo(Duration.ofSeconds(3))
                && Inventory.getCount(config.metal().getBarId()) < config.product().getRequiredBars();
    }

    @Override
    public void execute() {
        if (!open()) {
            return;
        }

        if (!Inventory.contains(ItemID.HAMMER, ItemID.IMCANDO_HAMMER) && !Equipment.contains(ItemID.IMCANDO_HAMMER)) {
            if (Bank.contains(ItemID.IMCANDO_HAMMER)) {
                Bank.withdraw(ItemID.IMCANDO_HAMMER, 1, Bank.WithdrawMode.ITEM);
            } else {
                Bank.withdraw(ItemID.HAMMER, 1, Bank.WithdrawMode.ITEM);
            }
        }

        Bank.depositAllExcept(ItemID.HAMMER, ItemID.IMCANDO_HAMMER, config.metal().getBarId());

        Bank.withdrawAll(config.metal().getBarId(), Bank.WithdrawMode.ITEM);
    }
}
