package io.reisub.unethicalite.farming.tasks;

import io.reisub.unethicalite.farming.Config;
import io.reisub.unethicalite.farming.Farming;
import io.reisub.unethicalite.farming.PatchImplementation;
import io.reisub.unethicalite.farming.PatchState;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.runelite.client.plugins.timetracking.farming.CropState;
import net.runelite.client.plugins.timetracking.farming.Produce;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.game.Vars;
import net.unethicalite.api.items.Inventory;

public class PlantLimpwurt extends Task {
  @Inject private Farming plugin;
  @Inject private Config config;

  @Override
  public String getStatus() {
    return "Planting limpwurt seeds";
  }

  @Override
  public boolean validate() {
    if (!config.limpwurt() || !Inventory.contains(ItemID.LIMPWURT_SEED)) {
      return false;
    }

    final TileObject patch = TileObjects.getNearest(Predicates.ids(Constants.FLOWER_PATCH_IDS));

    if (patch == null) {
      return false;
    }

    final int varbitValue = Vars.getBit(plugin.getCurrentLocation().getFlowerVarbit());
    final PatchState patchState = PatchImplementation.FLOWER.forVarbitValue(varbitValue);

    return patchState != null
        && patchState.getProduce() == Produce.WEEDS
        && patchState.getCropState() == CropState.GROWING;
  }

  @Override
  public int execute() {
    final TileObject patch = TileObjects.getNearest(Predicates.ids(Constants.FLOWER_PATCH_IDS));
    if (patch == null) {
      return 1;
    }

    final Item seed = Inventory.getFirst(ItemID.LIMPWURT_SEED);
    if (seed == null) {
      return 1;
    }

    GameThread.invoke(() -> seed.useOn(patch));

    if (!Time.sleepTicksUntil(
        () -> Vars.getBit(plugin.getCurrentLocation().getFlowerVarbit()) > 3, 20)) {
      return 1;
    }

    final Item compost = Inventory.getFirst(Predicates.ids(Constants.COMPOST_IDS));
    if (compost == null) {
      return 1;
    }

    GameThread.invoke(() -> compost.useOn(patch));
    return 4;
  }
}
