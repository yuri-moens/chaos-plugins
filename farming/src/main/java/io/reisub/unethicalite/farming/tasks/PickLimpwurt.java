package io.reisub.unethicalite.farming.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.game.Vars;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.farming.Config;
import io.reisub.unethicalite.farming.Farming;
import io.reisub.unethicalite.farming.PatchImplementation;
import io.reisub.unethicalite.farming.PatchState;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.TileObject;
import net.runelite.client.plugins.timetracking.farming.CropState;
import net.runelite.client.plugins.timetracking.farming.Produce;

public class PickLimpwurt extends Task {
  @Inject private Farming plugin;
  @Inject private Config config;

  @Override
  public String getStatus() {
    return "Picking limpwurt roots";
  }

  @Override
  public boolean validate() {
    if (!config.limpwurt() || Inventory.isFull()) {
      return false;
    }

    TileObject patch = TileObjects.getNearest(Predicates.ids(Constants.FLOWER_PATCH_IDS));

    if (patch == null) {
      return false;
    }

    int varbitValue = Vars.getBit(plugin.getCurrentLocation().getFlowerVarbit());
    PatchState patchState = PatchImplementation.FLOWER.forVarbitValue(varbitValue);

    return patchState != null
        && patchState.getProduce() == Produce.LIMPWURT
        && patchState.getCropState() == CropState.HARVESTABLE;
  }

  @Override
  public void execute() {
    TileObject patch = TileObjects.getNearest(Predicates.ids(Constants.FLOWER_PATCH_IDS));
    if (patch == null) {
      return;
    }

    GameThread.invoke(() -> patch.interact("Pick"));

    Time.sleepTicksUntil(
        () -> Inventory.isFull() || Vars.getBit(plugin.getCurrentLocation().getFlowerVarbit()) <= 3,
        100);
  }
}
