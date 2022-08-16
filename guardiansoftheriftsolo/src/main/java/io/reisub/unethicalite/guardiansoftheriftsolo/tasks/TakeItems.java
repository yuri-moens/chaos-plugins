package io.reisub.unethicalite.guardiansoftheriftsolo.tasks;

import io.reisub.unethicalite.guardiansoftheriftsolo.GuardiansOfTheRiftSolo;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class TakeItems extends Task {

  @Inject
  private GuardiansOfTheRiftSolo plugin;

  @Override
  public String getStatus() {
    return "Taking items";
  }

  @Override
  public boolean validate() {
    return !plugin.isGameActive()
        && (!Inventory.contains(ItemID.WEAK_CELL)
        || Inventory.getCount(true, ItemID.UNCHARGED_CELL) < 10);
  }

  @Override
  public void execute() {
    if (!Inventory.contains(ItemID.WEAK_CELL)) {
      final TileObject weakCells = TileObjects.getNearest(ObjectID.WEAK_CELLS);

      if (weakCells == null) {
        return;
      }

      weakCells.interact("Take");
      Time.sleepTicksUntil(() -> Inventory.contains(ItemID.WEAK_CELL), 20);
    }

    if (Inventory.getCount(true, ItemID.UNCHARGED_CELL) < 10) {
      final TileObject unchargedCells = TileObjects.getNearest(ObjectID.UNCHARGED_CELLS_43732);

      if (unchargedCells == null) {
        return;
      }

      unchargedCells.interact("Take-10");
      Time.sleepTicksUntil(() -> Inventory.getCount(true, ItemID.UNCHARGED_CELL) >= 10, 20);
    }
  }
}
