package io.reisub.unethicalite.giantsfoundry;

import io.reisub.unethicalite.giantsfoundry.enums.Alloy;
import io.reisub.unethicalite.giantsfoundry.enums.Heat;
import io.reisub.unethicalite.giantsfoundry.enums.Stage;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.game.Vars;

@Singleton
@Slf4j
public class GiantsFoundryState {
  // heat and progress are from 0-1000
  private static final int VARBIT_HEAT = 13948;
  private static final int VARBIT_PROGRESS = 13949;

  private static final int VARBIT_COMMISSION_1 = 13907;
  private static final int VARBIT_COMMISSION_2 = 13908;

  private static final int VARBIT_ORE_COUNT_START = 13931;
  private static final int VARBIT_FORTE_SELECTED = 13910;
  private static final int VARBIT_BLADE_SELECTED = 13911;
  private static final int VARBIT_TIP_SELECTED = 13912;

  // 0 - start
  // 1 - mould set
  // 2 - collect preform
  // 3 -
  private static final int VARBIT_GAME_STAGE = 13914;

  private static final int WIDGET_HEAT_PARENT = 49414153;
  private static final int WIDGET_LOW_HEAT_PARENT = 49414163;
  private static final int WIDGET_MED_HEAT_PARENT = 49414164;
  private static final int WIDGET_HIGH_HEAT_PARENT = 49414165;

  private static final int WIDGET_PROGRESS_PARENT = 49414219;
  private static final int WIDGET_BONUS_PARENT = 49414148;


  // children with type 3 are stage boxes
  // every 11th child is a sprite

  private static final int SPRITE_ID_TRIP_HAMMER = 4442;
  private static final int SPRITE_ID_GRINDSTONE = 4443;
  private static final int SPRITE_ID_POLISHING_WHEEL = 4444;
  private final List<Stage> stages = new ArrayList<>();
  @Inject
  private Client client;
  private double heatRangeRatio = 0;

  public void reset() {
    stages.clear();
    heatRangeRatio = 0;
  }

  public int getHeatAmount() {
    return Vars.getBit(VARBIT_HEAT);
  }

  public int getProgressAmount() {
    return Vars.getBit(VARBIT_PROGRESS);
  }

  public int getGameStage() {
    return Vars.getBit(VARBIT_GAME_STAGE);
  }

  public int getOreCount() {
    int barAmount = 0;
    for (int i = 0; i < Alloy.values().length; i++) {
      barAmount += Vars.getBit(VARBIT_ORE_COUNT_START + i);
    }
    return barAmount;
  }

  public int getFirstPartCommission() {
    return Vars.getBit(VARBIT_COMMISSION_1);
  }

  public int getSecondPartCommission() {
    return Vars.getBit(VARBIT_COMMISSION_2);
  }

  public double getHeatRangeRatio() {
    if (heatRangeRatio == 0) {
      Widget heatWidget = client.getWidget(WIDGET_HEAT_PARENT);
      Widget medHeat = client.getWidget(WIDGET_MED_HEAT_PARENT);
      if (medHeat == null || heatWidget == null) {
        return 0;
      }

      heatRangeRatio = medHeat.getWidth() / (double) heatWidget.getWidth();
    }

    return heatRangeRatio;
  }

  public int[] getLowHeatRange() {
    return new int[] {
        (int) ((1 / 6d - getHeatRangeRatio() / 2) * 1000),
        (int) ((1 / 6d + getHeatRangeRatio() / 2) * 1000),
    };
  }

  public int[] getMedHeatRange() {
    return new int[] {
        (int) ((3 / 6d - getHeatRangeRatio() / 2) * 1000),
        (int) ((3 / 6d + getHeatRangeRatio() / 2) * 1000),
    };
  }

  public int[] getHighHeatRange() {
    return new int[] {
        (int) ((5 / 6d - getHeatRangeRatio() / 2) * 1000),
        (int) ((5 / 6d + getHeatRangeRatio() / 2) * 1000),
    };
  }

  public List<Stage> getStages() {
    if (stages.isEmpty()) {
      Widget progressParent = client.getWidget(WIDGET_PROGRESS_PARENT);
      if (progressParent == null || progressParent.getChildren() == null) {
        return new ArrayList<>();
      }

      for (Widget child : progressParent.getChildren()) {
        switch (child.getSpriteId()) {
          case SPRITE_ID_TRIP_HAMMER:
            stages.add(Stage.TRIP_HAMMER);
            break;
          case SPRITE_ID_GRINDSTONE:
            stages.add(Stage.GRINDSTONE);
            break;
          case SPRITE_ID_POLISHING_WHEEL:
            stages.add(Stage.POLISHING_WHEEL);
            break;
          default:
            break;
        }
      }
    }

    return stages;
  }

  public Stage getCurrentStage() {
    int index = (int) (getProgressAmount() / 1000d * getStages().size());
    if (index < 0 || index > getStages().size() - 1) {
      return null;
    }

    return getStages().get(index);
  }

  public Heat getCurrentHeat() {
    int heat = getHeatAmount();

    int[] low = getLowHeatRange();
    if (heat > low[0] && heat < low[1]) {
      return Heat.LOW;
    }

    int[] med = getMedHeatRange();
    if (heat > med[0] && heat < med[1]) {
      return Heat.MED;
    }

    int[] high = getHighHeatRange();
    if (heat > high[0] && heat < high[1]) {
      return Heat.HIGH;
    }

    return Heat.NONE;
  }
}
