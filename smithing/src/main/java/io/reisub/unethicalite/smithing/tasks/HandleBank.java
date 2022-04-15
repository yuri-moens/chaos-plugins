package io.reisub.unethicalite.smithing.tasks;

import dev.unethicalite.api.items.Bank;
import dev.unethicalite.api.items.Equipment;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.smithing.Config;
import io.reisub.unethicalite.smithing.Smithing;
import io.reisub.unethicalite.utils.api.CBank;
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
        plugin.setActivity(Activity.BANKING);

        if (!open()) {
            plugin.setActivity(Activity.IDLE);
            return;
        }

        if (!Inventory.contains(ItemID.HAMMER, ItemID.IMCANDO_HAMMER) && !Equipment.contains(ItemID.IMCANDO_HAMMER)) {
            if (Bank.contains(ItemID.IMCANDO_HAMMER)) {
                Bank.withdraw(ItemID.IMCANDO_HAMMER, 1, Bank.WithdrawMode.ITEM);
            } else {
                Bank.withdraw(ItemID.HAMMER, 1, Bank.WithdrawMode.ITEM);
            }
        }

        CBank.depositAllExcept(false, ItemID.HAMMER, ItemID.IMCANDO_HAMMER, config.metal().getBarId());

        if (Bank.getCount(true, config.metal().getBarId()) >= config.product().getRequiredBars()) {
            Bank.withdrawAll(config.metal().getBarId(), Bank.WithdrawMode.ITEM);
            plugin.setActivity(Activity.IDLE);
        } else {
            plugin.stop("Out of bars. Stopping plugin.");
        }
    }
}
