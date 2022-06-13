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
public class HeatUp extends Task {

  @Inject
  private GiantsFoundry plugin;
  @Inject
  private GiantsFoundryState state;
  @Inject
  private GiantsFoundryHelper helper;
  private int last;

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

    if (Static.getClient().getTickCount() - last < 5) {
      return false;
    }

    return state.getHeatAmount()
        < helper.getCurrentHeatRange()[0];
  }

  @Override
  public void execute() {
    final int target = getTargetHeat();

    final TileObject lavaPool = TileObjects.getNearest("Lava pool");

    if (lavaPool == null) {
      return;
    }

    if (state.getHeatAmount() + GiantsFoundryHelper.DUNK_LAVA_HEAT < target) {
      GameThread.invoke(() -> lavaPool.interact("Dunk-preform"));

      Time.sleepTicksUntil(
          () -> state.getHeatAmount() + GiantsFoundryHelper.DUNK_LAVA_HEAT > target, 30);
    }

    if (state.getHeatAmount() + GiantsFoundryHelper.HEAT_LAVA_HEAT < target) {
      GameThread.invoke(() -> lavaPool.interact("Heat-preform"));

      Time.sleepTicksUntil(() -> state.getHeatAmount() > target, 30);
    }

    last = Static.getClient().getTickCount();
  }

  private int getTargetHeat() {
    final Stage currentStage = state.getCurrentStage();

    if (currentStage.getHeatChange() < 0) {
      final int targetForRemainingActions =
          helper.getCurrentHeatRange()[0]
              + (helper.getActionsLeftInStage() + 2) * -currentStage.getHeatChange();

      return Math.min(
          targetForRemainingActions,
          helper.getCurrentHeatRange()[1] - GiantsFoundryHelper.HEAT_LAVA_HEAT
      );
    } else {
      return helper.getCurrentHeatRange()[0] + GiantsFoundryHelper.HEAT_LAVA_HEAT * 2;
    }
  }
}