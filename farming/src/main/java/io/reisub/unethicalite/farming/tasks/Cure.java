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
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.TileObject;
import net.runelite.client.plugins.timetracking.farming.CropState;

public class Cure extends Task {
  @Inject
  private Farming plugin;

  @Override
  public String getStatus() {
    return "Curing herb";
  }

  @Override
  public boolean validate() {
    TileObject patch = TileObjects.getNearest(Predicates.ids(Constants.HERB_PATCH_IDS));

    if (patch == null) {
      return false;
    }

    int varbit = Vars.getBit(plugin.getCurrentLocation().getVarbit());
    PatchState patchState = PatchImplementation.HERB.forVarbitValue(varbit);

    return patchState != null && patchState.getCropState() == CropState.DISEASED;
  }

  @Override
  public void execute() {
    if (!Inventory.contains(ItemID.PLANT_CURE)) {
      NPC leprechaun = NPCs.getNearest("Tool Leprechaun");
      if (leprechaun == null) {
        return;
      }

      GameThread.invoke(() -> leprechaun.interact("Exchange"));
      Time.sleepTicksUntil(() -> Widgets.isVisible(Constants.TOOLS_WIDGET.get()), 30);
      Time.sleepTick();

      Constants.TOOLS_WITHDRAW_PLANT_CURE_WIDGET.get().interact(0);
      Constants.TOOLS_CLOSE_WIDGET.get().interact("Close");

      if (!Time.sleepTicksUntil(() -> Inventory.contains(ItemID.PLANT_CURE), 10)) {
        MessageUtils.addMessage("No plant cure found, can't cure herb.");
        plugin.getCurrentLocation().setDone(true);
        return;
      }
    }

    TileObject patch = TileObjects.getNearest(Predicates.ids(Constants.HERB_PATCH_IDS));
    Item plantCure = Inventory.getFirst(ItemID.PLANT_CURE);

    int plantCureCount = Inventory.getCount(ItemID.PLANT_CURE);

    GameThread.invoke(() -> plantCure.useOn(patch));
    Time.sleepTicksUntil(() -> Inventory.getCount(ItemID.PLANT_CURE) < plantCureCount, 30);
    plugin.getCurrentLocation().setDone(true);
  }
}
