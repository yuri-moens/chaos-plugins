package io.reisub.unethicalite.giantsfoundry.tasks;

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
public class Grind extends Task {
    @Inject private GiantsFoundry plugin;

    @Inject
    private GiantsFoundryState giantsFoundryState;

    @Inject
    private GiantsFoundryHelper giantsFoundryHelper;

    @Override
    public String getStatus() {
        return "Grinding";
    }

    @Override
    public boolean validate() {
        return giantsFoundryState.getCurrentStage().equals(Stage.GRINDSTONE);
    }

    @Override
    public void execute() {
        TileObject gs = TileObjects.getNearest("Grindstone");
        if (gs == null) {
            return;
        }

        gs.interact("Use");
        Time.sleepTicksUntil(() -> !giantsFoundryState.getCurrentStage().equals(Stage.GRINDSTONE) || giantsFoundryState.getHeatAmount() > giantsFoundryHelper.getCurrentHeatRange()[1], 30);

    }
}