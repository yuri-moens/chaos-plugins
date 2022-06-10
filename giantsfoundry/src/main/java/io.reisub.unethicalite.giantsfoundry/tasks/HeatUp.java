package io.reisub.unethicalite.giantsfoundry.tasks;

import io.reisub.unethicalite.giantsfoundry.Config;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundry;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryHelper;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryState;
import io.reisub.unethicalite.giantsfoundry.enums.Stage;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;

@Slf4j
public class HeatUp extends Task {
  @Inject
  private GiantsFoundry plugin;
  @Inject
  private GiantsFoundryState state;
  @Inject
  private GiantsFoundryHelper helper;
  @Inject
  private Config config;
  @Inject
  private Client client;

  @Override
  public String getStatus() {
    return "Heating the preform";
  }

  @Override
  public boolean validate() {
    Stage currentStage = state.getCurrentStage();
    if (currentStage == null) {
      return false;
    }

    return state.getHeatAmount() < helper.getCurrentHeatRange()[0];

  }

  @Override
  public void execute() {
    if (state.getCurrentStage().getHeatChange() < 0) {
      if (state.getHeatAmount() + GiantsFoundryHelper.DUNK_LAVA_HEAT
          <
          helper.getCurrentHeatRange()[1]) {
        TileObject lp = TileObjects.getNearest("Lava pool");
        if (lp != null) {
          lp.interact("Dunk-preform");
          Time.sleepTicksUntil(
              () -> state.getHeatAmount() + GiantsFoundryHelper.DUNK_LAVA_HEAT * 2
                  >
                  helper.getCurrentHeatRange()[1], 20);
        }
      }

      if (state.getHeatAmount() + GiantsFoundryHelper.HEAT_LAVA_HEAT
          <
          helper.getCurrentHeatRange()[1]) {
        TileObject lp = TileObjects.getNearest("Lava pool");
        if (lp != null) {
          lp.interact("Heat-preform");
          Time.sleepTicksUntil(
              () -> state.getHeatAmount() + GiantsFoundryHelper.HEAT_LAVA_HEAT * 2
                  >
                  helper.getCurrentHeatRange()[1], 20);
        }
      }
    } else {
      if (state.getHeatAmount() + GiantsFoundryHelper.DUNK_LAVA_HEAT
          <
          helper.getCurrentHeatRange()[0]) {
        TileObject lp = TileObjects.getNearest("Lava pool");
        if (lp != null) {
          lp.interact("Dunk-preform");
          Time.sleepTicksUntil(
              () -> state.getHeatAmount() + GiantsFoundryHelper.DUNK_LAVA_HEAT * 2
                  >
                  helper.getCurrentHeatRange()[0], 20);
        }
      }

      if (state.getHeatAmount() + GiantsFoundryHelper.HEAT_LAVA_HEAT
          <
          helper.getCurrentHeatRange()[0]) {
        TileObject lp = TileObjects.getNearest("Lava pool");
        if (lp != null) {
          lp.interact("Heat-preform");
          Time.sleepTicksUntil(
              () -> state.getHeatAmount() + GiantsFoundryHelper.HEAT_LAVA_HEAT * 2
                  >
                  helper.getCurrentHeatRange()[0], 20);
        }
      }
    }

  }
}