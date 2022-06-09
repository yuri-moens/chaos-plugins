package io.reisub.unethicalite.giantsfoundry.tasks;

import io.reisub.unethicalite.giantsfoundry.Config;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundry;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryHelper;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryState;
import io.reisub.unethicalite.giantsfoundry.enums.Stage;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.RequiredArgsConstructor;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;

import javax.inject.Inject;

@RequiredArgsConstructor
public class Hammer extends Task {
    @Inject private GiantsFoundry plugin;

    @Inject
    private GiantsFoundryState giantsFoundryState;

    @Inject
    private GiantsFoundryHelper giantsFoundryHelper;

    @Override
    public String getStatus() {
        return "Hammering";
    }

    @Override
    public boolean validate() {
        return giantsFoundryState.getCurrentStage().equals(Stage.TRIP_HAMMER);
    }

    @Override
    public void execute() {
        TileObject th = TileObjects.getNearest("Trip hammer");
        if (th == null) {
            return;
        }

        th.interact("Use");
        Time.sleepTicksUntil(() -> !giantsFoundryState.getCurrentStage().equals(Stage.TRIP_HAMMER) || giantsFoundryState.getHeatAmount() < giantsFoundryHelper.getCurrentHeatRange()[0], 30);

    }
}