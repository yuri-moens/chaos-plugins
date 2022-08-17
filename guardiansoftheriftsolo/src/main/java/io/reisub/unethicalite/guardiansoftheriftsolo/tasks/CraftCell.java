package io.reisub.unethicalite.guardiansoftheriftsolo.tasks;

import io.reisub.unethicalite.guardiansoftheriftsolo.data.AreaType;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class CraftCell extends Task {

  @Override
  public String getStatus() {
    return "Crafting cell";
  }

  @Override
  public boolean validate() {
    return AreaType.getCurrent() == AreaType.ALTAR
        && !Inventory.contains(Predicates.ids(Constants.CELL_IDS))
        && !Inventory.contains(ItemID.GUARDIAN_ESSENCE);
  }

  @Override
  public void execute() {
    final TileObject altar = TileObjects.getNearest("Altar");

    if (altar == null) {
      return;
    }

    altar.interact("Craft-rune");
    Time.sleepTicksUntil(() -> Inventory.contains(Predicates.ids(Constants.CELL_IDS)), 20);
  }
}
