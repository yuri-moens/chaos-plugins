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
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class MineHugeRemains extends Task {

  @Inject
  private GuardiansOfTheRiftSolo plugin;

  @Override
  public String getStatus() {
    return "Mining huge remains";
  }

  @Override
  public boolean validate() {
    return plugin.isGameActive()
        && AreaType.getCurrent() == AreaType.HUGE_REMAINS
        && !plugin.isCurrentActivity(PluginActivity.MINING)
        && (!Inventory.isFull() || !plugin.arePouchesFull());
  }

  @Override
  public void execute() {
    if (Inventory.contains(ItemID.GUARDIAN_ESSENCE) && !plugin.arePouchesFull()) {
      for (Item pouch : Inventory.getAll(Predicates.ids(Constants.ESSENCE_POUCH_IDS))) {
        pouch.interact("Fill");
      }
    }

    final TileObject remains = TileObjects.getNearest(ObjectID.HUGE_GUARDIAN_REMAINS);

    if (remains == null) {
      return;
    }

    remains.interact("Mine");
    Time.sleepTicksUntil(() -> plugin.isCurrentActivity(PluginActivity.MINING), 20);
  }
}
