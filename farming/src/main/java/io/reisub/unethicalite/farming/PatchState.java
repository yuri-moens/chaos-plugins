package io.reisub.unethicalite.farming;

import lombok.Value;
import net.runelite.client.plugins.timetracking.farming.CropState;
import net.runelite.client.plugins.timetracking.farming.Produce;

@Value
public class PatchState {
  Produce produce;
  CropState cropState;
  int stage;

  public int getStages() {
    return cropState == CropState.HARVESTABLE || cropState == CropState.FILLING
        ? produce.getHarvestStages()
        : produce.getStages();
  }

  public int getTickRate() {
    switch (cropState) {
      case HARVESTABLE:
        return produce.getRegrowTickrate();
      case GROWING:
        return produce.getTickrate();
      default:
        return 0;
    }
  }
}
