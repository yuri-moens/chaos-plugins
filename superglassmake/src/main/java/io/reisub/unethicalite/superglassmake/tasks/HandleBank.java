package io.reisub.unethicalite.superglassmake.tasks;

import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileItems;
import dev.hoot.api.items.Bank;
import dev.hoot.api.items.Inventory;
import io.reisub.unethicalite.superglassmake.Config;
import io.reisub.unethicalite.superglassmake.SuperglassMake;
import io.reisub.unethicalite.utils.api.CBank;
import io.reisub.unethicalite.utils.tasks.BankTask;
import net.runelite.api.ItemID;

import javax.inject.Inject;
import java.time.Duration;

public class HandleBank extends BankTask {
    @Inject
    private SuperglassMake plugin;

    @Inject
    private Config config;

    @Override
    public boolean validate() {
        return isLastBankDurationAgo(Duration.ofSeconds(2))
                && (!Inventory.contains(ItemID.BUCKET_OF_SAND) || !Inventory.contains(ItemID.GIANT_SEAWEED, ItemID.SEAWEED, ItemID.SODA_ASH));
    }

    @Override
    public void execute() {
        open();

        CBank.depositAllExcept(false, ItemID.ASTRAL_RUNE, ItemID.AIR_RUNE, ItemID.FIRE_RUNE, ItemID.RUNE_POUCH);

        if (TileItems.getAt(Players.getLocal().getWorldLocation(), ItemID.MOLTEN_GLASS).size() >= 27) {
            close();
            return;
        }

        if (!Bank.contains(ItemID.BUCKET_OF_SAND) || (!Bank.contains(ItemID.SODA_ASH) && !Bank.contains(ItemID.GIANT_SEAWEED))) {
            plugin.stop("Out of materials. Stopping plugin.");
        }

        if (config.useSodaAshFirst() && Bank.contains(ItemID.SODA_ASH)) {
            Bank.withdraw(ItemID.BUCKET_OF_SAND, 13, Bank.WithdrawMode.ITEM);
            Bank.withdraw(ItemID.SODA_ASH, 13, Bank.WithdrawMode.ITEM);
        } else {
            Bank.withdraw(ItemID.BUCKET_OF_SAND, 18, Bank.WithdrawMode.ITEM);

            for (int i = 0; i < 3; i++) {
                Bank.withdraw(ItemID.GIANT_SEAWEED, 1, Bank.WithdrawMode.ITEM);
            }
        }

        close();
    }
}
