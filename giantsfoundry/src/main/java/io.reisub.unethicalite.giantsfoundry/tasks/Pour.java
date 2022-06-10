package io.reisub.unethicalite.giantsfoundry.tasks;

import io.reisub.unethicalite.giantsfoundry.GiantsFoundry;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryHelper;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryState;
import io.reisub.unethicalite.giantsfoundry.enums.Stage;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Equipment;

import javax.inject.Inject;

public class Pour extends Task {
    @Inject private GiantsFoundry plugin;

    @Inject
    GiantsFoundryState giantsFoundryState;

    @Inject
    GiantsFoundryHelper giantsFoundryHelper;

    @Override
    public String getStatus() {
        return "Pouring metal";
    }

    @Override
    public boolean validate() {
        return giantsFoundryState.getGameStage() == 1 && giantsFoundryState.getOreCount() == 28;

    }

    @Override
    public void execute() {
        TileObject crucible = TileObjects.getNearest("Crucible (full)");
        if (crucible == null) {
            return;
        }
        crucible.interact("Pour");
        Time.sleepTicksUntil(() -> giantsFoundryState.getOreCount() == 0, 20);
        Time.sleepTick();
    }
}