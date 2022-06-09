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
public class HeatUp extends Task {
    @Inject private GiantsFoundry plugin;

    @Inject
    GiantsFoundryState giantsFoundryState;

    @Inject
    GiantsFoundryHelper giantsFoundryHelper;

    @Override
    public String getStatus() {
        return "Heating the preform";
    }

    @Override
    public boolean validate() {
        Stage currentStage = giantsFoundryState.getCurrentStage();
        if (currentStage == null) {
            return false;
        }

        return giantsFoundryState.getHeatAmount() < giantsFoundryHelper.getCurrentHeatRange()[0];

    }

    @Override
    public void execute() {
        if (giantsFoundryState.getCurrentStage().getHeatChange() < 0) {
            if (giantsFoundryState.getHeatAmount() + GiantsFoundryHelper.DUNK_LAVA_HEAT < giantsFoundryHelper.getCurrentHeatRange()[1]) {
                TileObject lp = TileObjects.getNearest("Lava pool");
                if (lp != null) {
                    lp.interact("Dunk-preform");
                    Time.sleepTicksUntil(() -> giantsFoundryState.getHeatAmount() + GiantsFoundryHelper.DUNK_LAVA_HEAT > giantsFoundryHelper.getCurrentHeatRange()[1], 20);
                }
            }

            if (giantsFoundryState.getHeatAmount() + GiantsFoundryHelper.HEAT_LAVA_HEAT < giantsFoundryHelper.getCurrentHeatRange()[1]) {
                TileObject lp = TileObjects.getNearest("Lava pool");
                if (lp != null) {
                    lp.interact("Heat-preform");
                    Time.sleepTicksUntil(() -> giantsFoundryState.getHeatAmount() + GiantsFoundryHelper.HEAT_LAVA_HEAT > giantsFoundryHelper.getCurrentHeatRange()[1], 20);
                }
            }
        } else {
            if (giantsFoundryState.getHeatAmount() + GiantsFoundryHelper.DUNK_LAVA_HEAT < giantsFoundryHelper.getCurrentHeatRange()[0]) {
                TileObject lp = TileObjects.getNearest("Lava pool");
                if (lp != null) {
                    lp.interact("Dunk-preform");
                    Time.sleepTicksUntil(() -> giantsFoundryState.getHeatAmount() + GiantsFoundryHelper.DUNK_LAVA_HEAT > giantsFoundryHelper.getCurrentHeatRange()[0], 20);
                }
            }

            if (giantsFoundryState.getHeatAmount() + GiantsFoundryHelper.HEAT_LAVA_HEAT < giantsFoundryHelper.getCurrentHeatRange()[0]) {
                TileObject lp = TileObjects.getNearest("Lava pool");
                if (lp != null) {
                    lp.interact("Heat-preform");
                    Time.sleepTicksUntil(() -> giantsFoundryState.getHeatAmount() + GiantsFoundryHelper.HEAT_LAVA_HEAT > giantsFoundryHelper.getCurrentHeatRange()[0], 20);
                }
            }
        }

    }
}