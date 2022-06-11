package io.reisub.unethicalite.giantsfoundry.tasks;

import io.reisub.unethicalite.giantsfoundry.GiantsFoundry;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryHelper;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryState;
import io.reisub.unethicalite.giantsfoundry.enums.Stage;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;

@Slf4j
public class CoolDown extends Task {
  @Inject
  private GiantsFoundry plugin;

  @Inject
  private GiantsFoundryState giantsFoundryState;

  @Inject
  private GiantsFoundryHelper giantsFoundryHelper;

  @Override
  public String getStatus() {
    return "Cooling the preform";
  }

  @Override
  public boolean validate() {
    Stage currentStage = giantsFoundryState.getCurrentStage();
    if (currentStage == null) {
      return false;
    }

    return giantsFoundryState.getHeatAmount() > giantsFoundryHelper.getCurrentHeatRange()[1];

  }

  @Override
  public void execute() {
    if (giantsFoundryState.getCurrentStage().getHeatChange() > 0) {
      if (giantsFoundryState.getHeatAmount() + GiantsFoundryHelper.QUENCH_WATERFALL_HEAT
          >
          giantsFoundryHelper.getCurrentHeatRange()[0]) {
        TileObject lp = TileObjects.getNearest("Waterfall");
        if (lp != null) {
          lp.interact("Quench-preform");
          Time.sleepTicksUntil(() ->
              giantsFoundryState.getHeatAmount() + GiantsFoundryHelper.QUENCH_WATERFALL_HEAT * 2
                  <
                  giantsFoundryHelper.getCurrentHeatRange()[0], 20);
        }
      }

      if (giantsFoundryState.getHeatAmount() + GiantsFoundryHelper.COOL_WATERFALL_HEAT
          >
          giantsFoundryHelper.getCurrentHeatRange()[0]) {
        TileObject lp = TileObjects.getNearest("Waterfall");
        if (lp != null) {
          lp.interact("Cool-preform");
          Time.sleepTicksUntil(() ->
              giantsFoundryState.getHeatAmount() + GiantsFoundryHelper.COOL_WATERFALL_HEAT * 2
                  <
                  giantsFoundryHelper.getCurrentHeatRange()[0], 20);
        }
      }
    } else {
      if (giantsFoundryState.getHeatAmount() + GiantsFoundryHelper.QUENCH_WATERFALL_HEAT
          >
          giantsFoundryHelper.getCurrentHeatRange()[1]) {
        TileObject lp = TileObjects.getNearest("Waterfall");
        if (lp != null) {
          lp.interact("Quench-preform");
          Time.sleepTicksUntil(() ->
              giantsFoundryState.getHeatAmount() + GiantsFoundryHelper.QUENCH_WATERFALL_HEAT * 2
                  <
                  giantsFoundryHelper.getCurrentHeatRange()[1], 20);
        }
      }

      if (giantsFoundryState.getHeatAmount() + GiantsFoundryHelper.COOL_WATERFALL_HEAT
          >
          giantsFoundryHelper.getCurrentHeatRange()[1]) {
        TileObject lp = TileObjects.getNearest("Waterfall");
        if (lp != null) {
          lp.interact("Cool-preform");
          Time.sleepTicksUntil(() ->
              giantsFoundryState.getHeatAmount() + GiantsFoundryHelper.COOL_WATERFALL_HEAT * 2
                  <
                  giantsFoundryHelper.getCurrentHeatRange()[1], 20);
        }
      } else {
        Time.sleepTicksUntil(
            () -> giantsFoundryState.getHeatAmount() < giantsFoundryHelper.getCurrentHeatRange()[0],
            15);
      }
    }

  }
}