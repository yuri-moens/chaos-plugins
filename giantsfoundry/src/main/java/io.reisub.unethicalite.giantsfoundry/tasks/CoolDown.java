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
import net.unethicalite.api.game.GameThread;
import net.unethicalite.client.Static;

@Slf4j
public class CoolDown extends Task {

  @Inject
  private GiantsFoundry plugin;
  @Inject
  private GiantsFoundryState state;
  @Inject
  private GiantsFoundryHelper helper;
  private int last;

  @Override
  public String getStatus() {
    return "Cooling the preform";
  }

  @Override
  public boolean validate() {
    Stage currentStage = state.getCurrentStage();
    if (currentStage == null) {
      return false;
    }

    if (Static.getClient().getTickCount() - last < 5) {
      return false;
    }

    return state.getHeatAmount()
        > helper.getCurrentHeatRange()[1];
  }

  @Override
  public int execute() {
    final int target = getTargetHeat();

    final TileObject waterfall = TileObjects.getNearest("Waterfall");

    if (waterfall == null) {
      return 1;
    }

    if (state.getHeatAmount() - Math.abs(GiantsFoundryHelper.QUENCH_WATERFALL_HEAT) > target) {
      GameThread.invoke(() -> waterfall.interact("Quench-preform"));

      Time.sleepTicksUntil(
          () -> state.getHeatAmount() - Math.abs(GiantsFoundryHelper.QUENCH_WATERFALL_HEAT)
              < target, 30);
    }

    if (state.getHeatAmount() - Math.abs(GiantsFoundryHelper.COOL_WATERFALL_HEAT) > target) {
      GameThread.invoke(() -> waterfall.interact("Cool-preform"));

      Time.sleepTicksUntil(() -> state.getHeatAmount() < target, 30);
    }

    last = Static.getClient().getTickCount();

    return 1;
  }

  private int getTargetHeat() {
    final Stage currentStage = state.getCurrentStage();

    if (currentStage.getHeatChange() > 0) {
      final int targetForRemainingActions =
          helper.getCurrentHeatRange()[1]
              - (helper.getActionsLeftInStage() + 2) * currentStage.getHeatChange();

      return Math.max(
          targetForRemainingActions,
          helper.getCurrentHeatRange()[0] + Math.abs(GiantsFoundryHelper.COOL_WATERFALL_HEAT) * 2
      );
    } else {
      return helper.getCurrentHeatRange()[1]
          - Math.abs(GiantsFoundryHelper.COOL_WATERFALL_HEAT);
    }
  }
}