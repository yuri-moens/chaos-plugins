package io.reisub.unethicalite.guardiansoftheriftsolo.tasks;

import io.reisub.unethicalite.guardiansoftheriftsolo.GuardiansOfTheRiftSolo;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.AreaType;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class TakeCell extends Task {

  @Inject
  private GuardiansOfTheRiftSolo plugin;

  @Override
  public String getStatus() {
    return "Taking cell";
  }

  @Override
  public boolean validate() {
    return plugin.isGameActive()
        && AreaType.getCurrent() == AreaType.MAIN
        && !Inventory.contains(Predicates.ids(Constants.CELL_IDS))
        && plugin.getElapsedTicks() < 115 / 0.6;
  }

  @Override
  public void execute() {
    final TileObject weakCells = TileObjects.getNearest(ObjectID.WEAK_CELLS);

    if (weakCells == null) {
      return;
    }

    weakCells.interact("Take");
    Time.sleepTicksUntil(() -> Inventory.contains(ItemID.WEAK_CELL), 20);
  }
}
