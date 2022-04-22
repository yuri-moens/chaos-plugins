package io.reisub.unethicalite.farming.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.game.Vars;
import io.reisub.unethicalite.farming.Farming;
import io.reisub.unethicalite.farming.PatchImplementation;
import io.reisub.unethicalite.farming.PatchState;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.TileObject;
import net.runelite.client.plugins.timetracking.farming.CropState;

public class Clear extends Task {
  @Inject private Farming plugin;

  @Override
  public String getStatus() {
    return "Clearing dead plants";
  }

  @Override
  public boolean validate() {
    return !getDeadPatches().isEmpty();
  }

  @Override
  public void execute() {
    getDeadPatches()
        .forEach(
            o -> {
              GameThread.invoke(() -> o.interact("Clear"));

              if (Constants.HERB_PATCH_IDS.contains(o.getId())) {
                Time.sleepTicksUntil(
                    () -> Vars.getBit(plugin.getCurrentLocation().getHerbVarbit()) <= 3, 20);
              } else if (Constants.FLOWER_PATCH_IDS.contains(o.getId())) {
                Time.sleepTicksUntil(
                    () -> Vars.getBit(plugin.getCurrentLocation().getFlowerVarbit()) <= 3, 20);
              }
            });
  }

  private List<TileObject> getDeadPatches() {
    final TileObject herbPatch = TileObjects.getNearest(Predicates.ids(Constants.HERB_PATCH_IDS));
    final TileObject flowerPatch =
        TileObjects.getNearest(Predicates.ids(Constants.FLOWER_PATCH_IDS));

    if (herbPatch == null && flowerPatch == null) {
      return Collections.emptyList();
    }

    final List<TileObject> deadPatches = new ArrayList<>(2);

    if (herbPatch != null) {
      final int herbVarbitValue = Vars.getBit(plugin.getCurrentLocation().getHerbVarbit());
      final PatchState patchState = PatchImplementation.HERB.forVarbitValue(herbVarbitValue);

      if (patchState != null && patchState.getCropState() == CropState.DEAD) {
        deadPatches.add(herbPatch);
      }
    }

    if (flowerPatch != null) {
      final int flowerVarbitValue = Vars.getBit(plugin.getCurrentLocation().getFlowerVarbit());
      final PatchState patchState = PatchImplementation.FLOWER.forVarbitValue(flowerVarbitValue);

      if (patchState != null && patchState.getCropState() == CropState.DEAD) {
        deadPatches.add(flowerPatch);
      }
    }

    return deadPatches;
  }
}
