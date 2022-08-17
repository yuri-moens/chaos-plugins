package io.reisub.unethicalite.guardiansoftheriftsolo.tasks;

import io.reisub.unethicalite.guardiansoftheriftsolo.GuardiansOfTheRiftSolo;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.AreaType;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.PluginActivity;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class CraftEssence extends Task {

  @Inject
  private GuardiansOfTheRiftSolo plugin;

  @Override
  public String getStatus() {
    return "Crafting essence";
  }

  @Override
  public boolean validate() {
    return plugin.isGameActive()
        && AreaType.getCurrent() == AreaType.MAIN
        && Inventory.contains(ItemID.GUARDIAN_FRAGMENTS)
        && !plugin.isCurrentActivity(PluginActivity.CRAFTING)
        && (!Inventory.isFull() || !plugin.arePouchesFull());
  }

  @Override
  public void execute() {
    if (Inventory.contains(ItemID.GUARDIAN_ESSENCE) && !plugin.arePouchesFull()) {
      for (Item pouch : Inventory.getAll(Predicates.ids(Constants.ESSENCE_POUCH_IDS))) {
        pouch.interact("Fill");
      }
    }

    final TileObject workbench = TileObjects.getNearest(ObjectID.WORKBENCH_43754);

    if (workbench == null) {
      return;
    }

    workbench.interact("Work-at");
    if (!Time.sleepTicksUntil(() -> Players.getLocal().getAnimation() == 9365, 20)) {
      return;
    }

    plugin.setActivity(PluginActivity.CRAFTING);
  }
}
