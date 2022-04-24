package io.reisub.unethicalite.farming.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.NPCs;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.game.Vars;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.utils.MessageUtils;
import dev.unethicalite.api.widgets.Widgets;
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
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.TileObject;
import net.runelite.client.plugins.timetracking.farming.CropState;

public class Cure extends Task {
  @Inject private Farming plugin;

  @Override
  public String getStatus() {
    return "Curing patch";
  }

  @Override
  public boolean validate() {
    return !getDiseasedPatches().isEmpty();
  }

  @Override
  public void execute() {
    final List<TileObject> diseasedPatches = getDiseasedPatches();

    if (!Inventory.contains(ItemID.PLANT_CURE)) {
      NPC leprechaun = NPCs.getNearest("Tool Leprechaun");
      if (leprechaun == null) {
        return;
      }

      GameThread.invoke(() -> leprechaun.interact("Exchange"));
      Time.sleepTicksUntil(() -> Widgets.isVisible(Constants.TOOLS_WIDGET.get()), 30);
      Time.sleepTick();

      for (int i = 0; i < diseasedPatches.size(); i++) {
        Constants.TOOLS_WITHDRAW_PLANT_CURE_WIDGET.get().interact(0);
      }
      Constants.TOOLS_CLOSE_WIDGET.get().interact("Close");

      if (!Time.sleepTicksUntil(() -> Inventory.contains(ItemID.PLANT_CURE), 5)) {
        MessageUtils.addMessage("No plant cure found, can't cure diseased plant.");
        plugin.getCurrentLocation().setSkip(true);
        return;
      }
    }

    diseasedPatches.forEach(
        o -> {
          Item plantCure = Inventory.getFirst(ItemID.PLANT_CURE);
          int plantCureCount = Inventory.getCount(ItemID.PLANT_CURE);

          GameThread.invoke(() -> plantCure.useOn(o));
          Time.sleepTicksUntil(() -> Inventory.getCount(ItemID.PLANT_CURE) < plantCureCount, 30);
        });
  }

  private List<TileObject> getDiseasedPatches() {
    final TileObject herbPatch = TileObjects.getNearest(Predicates.ids(Constants.HERB_PATCH_IDS));
    final TileObject flowerPatch =
        TileObjects.getNearest(Predicates.ids(Constants.FLOWER_PATCH_IDS));

    if (herbPatch == null && flowerPatch == null) {
      return Collections.emptyList();
    }

    final List<TileObject> diseasedPatches = new ArrayList<>(2);

    if (herbPatch != null) {
      final int herbVarbitValue = Vars.getBit(plugin.getCurrentLocation().getHerbVarbit());
      final PatchState patchState = PatchImplementation.HERB.forVarbitValue(herbVarbitValue);

      if (patchState != null && patchState.getCropState() == CropState.DISEASED) {
        diseasedPatches.add(herbPatch);
      }
    }

    if (flowerPatch != null) {
      final int flowerVarbitValue = Vars.getBit(plugin.getCurrentLocation().getFlowerVarbit());
      final PatchState patchState = PatchImplementation.FLOWER.forVarbitValue(flowerVarbitValue);

      if (patchState != null && patchState.getCropState() == CropState.DISEASED) {
        diseasedPatches.add(flowerPatch);
      }
    }

    return diseasedPatches;
  }
}
