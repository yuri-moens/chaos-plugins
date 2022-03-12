package io.reisub.unethicalite.shopper.tasks;

import dev.hoot.api.items.Inventory;
import dev.hoot.api.movement.Movement;
import io.reisub.unethicalite.shopper.Config;
import io.reisub.unethicalite.shopper.Shopper;
import io.reisub.unethicalite.utils.api.CBank;
import io.reisub.unethicalite.utils.api.CMovement;
import io.reisub.unethicalite.utils.tasks.BankTask;

import javax.inject.Inject;
import java.time.Duration;

public class HandleBank extends BankTask {
    @Inject
    private Shopper plugin;

    @Inject
    private Config config;

    @Override
    public boolean validate() {
        return Inventory.isFull()
                && isLastBankDurationAgo(Duration.ofSeconds(3));
    }

    @Override
    public void execute() {
        if (config.disableRunFromShop() && Movement.isRunEnabled() && Movement.getRunEnergy() < 90) {
            Movement.toggleRun();
        }

        if (plugin.getBankLocation() != null) {
            CMovement.walkTo(plugin.getBankLocation(), 1);
        }

        if (!open()) {
            return;
        }

        CBank.depositAllExcept(false, "Coins", "Tokkul");
    }
}
