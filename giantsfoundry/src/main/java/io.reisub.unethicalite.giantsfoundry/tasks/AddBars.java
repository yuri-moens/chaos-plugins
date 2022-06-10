package io.reisub.unethicalite.giantsfoundry.tasks;

import io.reisub.unethicalite.giantsfoundry.Config;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundry;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryHelper;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryState;
import io.reisub.unethicalite.giantsfoundry.enums.Alloy;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Dialog;
import net.unethicalite.api.widgets.Production;

import javax.inject.Inject;

public class AddBars extends Task {
    @Inject private GiantsFoundry plugin;
    @Inject private Config config;

    @Inject
    GiantsFoundryState giantsFoundryState;

    @Inject
    GiantsFoundryHelper giantsFoundryHelper;

    @Override
    public String getStatus() {
        return "Adding bars";
    }

    @Override
    public boolean validate() {
        return giantsFoundryState.getGameStage() == 1 && giantsFoundryState.getOreCount() < 28 && (Inventory.contains(config.alloy1().getBarId()) || Inventory.contains(config.alloy2().getBarId()));

    }

    @Override
    public void execute() {
        TileObject crucible = TileObjects.getNearest("Crucible (empty)");
        if (crucible == null) {
            return;
        }

        Alloy bar;

        if (Inventory.contains(config.alloy1().getBarId())) {
            bar = config.alloy1();
        } else {
            bar = config.alloy2();
        }
        crucible.interact("Fill");
        Time.sleepTicksUntil(Production::isOpen, 10);
        System.out.println(Dialog.getOptions());
        Production.chooseOption(bar.getDialogIndex());
        Time.sleepTicksUntil(() -> Players.getLocal().isIdle(), 10);
    }
}