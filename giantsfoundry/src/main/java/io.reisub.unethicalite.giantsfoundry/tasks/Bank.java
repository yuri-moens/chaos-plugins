package io.reisub.unethicalite.giantsfoundry.tasks;

import io.reisub.unethicalite.giantsfoundry.Config;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundry;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryHelper;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryState;
import io.reisub.unethicalite.giantsfoundry.enums.Alloy;
import io.reisub.unethicalite.giantsfoundry.enums.Stage;
import io.reisub.unethicalite.utils.api.ChaosBank;
import io.reisub.unethicalite.utils.tasks.BankTask;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

import javax.inject.Inject;

public class Bank extends BankTask {
    @Inject private GiantsFoundry plugin;
    @Inject private Config config;

    @Inject
    GiantsFoundryState giantsFoundryState;

    @Inject
    GiantsFoundryHelper giantsFoundryHelper;

    @Override
    public String getStatus() {
        return "Banking";
    }

    @Override
    public boolean validate() {
        return !Inventory.isFull() && giantsFoundryState.getGameStage() == 1 && giantsFoundryState.getOreCount() == 0;

    }

    @Override
    public void execute() {
        open(15, 10);
        net.unethicalite.api.items.Bank.depositInventory();

        Alloy alloy1 = config.alloy1();
        Alloy alloy2 = config.alloy2();

        if (alloy1.equals(alloy2)) {
            net.unethicalite.api.items.Bank.withdrawAll(alloy1.getBarId(), net.unethicalite.api.items.Bank.WithdrawMode.ITEM);
        } else {
            net.unethicalite.api.items.Bank.withdraw(alloy1.getBarId(), 14, net.unethicalite.api.items.Bank.WithdrawMode.ITEM);
            net.unethicalite.api.items.Bank.withdraw(alloy2.getBarId(), 14, net.unethicalite.api.items.Bank.WithdrawMode.ITEM);
        }

        close();
    }
}