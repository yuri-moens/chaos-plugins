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
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.runelite.client.plugins.timetracking.farming.CropState;
import net.runelite.client.plugins.timetracking.farming.Produce;

public class PlantLimpwurt extends Task {
  @Inject private Farming plugin;
  @Inject private Config config;

  @Override
  public String getStatus() {
    return "Planting limpwurt seeds";
  }

  @Override
  public boolean validate() {
    if (!config.limpwurt()) {
      return false;
    }

    final TileObject patch = TileObjects.getNearest(Predicates.ids(Constants.FLOWER_PATCH_IDS));

    if (patch == null) {
      return false;
    }

    final int varbitValue = Vars.getBit(plugin.getCurrentLocation().getFlowerVarbit());
    final PatchState patchState = PatchImplementation.FLOWER.forVarbitValue(varbitValue);

    return !plugin.getCurrentLocation().isDone()
        && patchState != null
        && patchState.getProduce() == Produce.WEEDS
        && patchState.getCropState() == CropState.GROWING;
  }

  @Override
  public void execute() {
    final TileObject patch = TileObjects.getNearest(Predicates.ids(Constants.FLOWER_PATCH_IDS));
    if (patch == null) {
      return;
    }

    final Item seed = Inventory.getFirst(ItemID.LIMPWURT_SEED);
    if (seed == null) {
      return;
    }

    GameThread.invoke(() -> seed.useOn(patch));

    if (!Time.sleepTicksUntil(
        () -> Vars.getBit(plugin.getCurrentLocation().getFlowerVarbit()) > 3, 20)) {
      return;
    }

    final Item compost = Inventory.getFirst(Predicates.ids(Constants.COMPOST_IDS));
    if (compost == null) {
      return;
    }

    GameThread.invoke(() -> compost.useOn(patch));
    Time.sleepTicks(3);
  }
}
