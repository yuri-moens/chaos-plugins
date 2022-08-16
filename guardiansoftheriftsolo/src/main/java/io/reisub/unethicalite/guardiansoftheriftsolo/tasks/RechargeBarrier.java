package io.reisub.unethicalite.guardiansoftheriftsolo.tasks;

import io.reisub.unethicalite.guardiansoftheriftsolo.GuardiansOfTheRiftSolo;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.AreaType;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class RechargeBarrier extends Task {

  @Inject
  private GuardiansOfTheRiftSolo plugin;

  @Override
  public String getStatus() {
    return "Recharging barrier";
  }

  @Override
  public boolean validate() {
    if (!plugin.isGameActive()
        || AreaType.getCurrent() != AreaType.MAIN
        || !Inventory.contains(Predicates.ids(Constants.CELL_IDS))) {
      return false;
    }

    if (plugin.getElapsedTicks() >= 115 / 0.6 && plugin.getElapsedTicks() <= 119 / 0.6) {
      return true;
    }

    if (Inventory.contains(ItemID.WEAK_CELL, ItemID.MEDIUM_CELL)
        && plugin.getElapsedTicks() > 130 / 0.6
        && plugin.getElapsedTicks() < 150 / 0.6) {
      return true;
    }

    return false;
  }

  @Override
  public void execute() {
    final TileObject cellTile = getCellTile();

    if (cellTile == null) {
      return;
    }

    cellTile.interact("Place-cell");
    Time.sleepTicksUntil(() -> !Inventory.contains(Predicates.ids(Constants.CELL_IDS)), 20);
  }

  private TileObject getCellTile() {
    if (plugin.getElapsedTicks() >= 115 / 0.6 && plugin.getElapsedTicks() <= 119 / 0.6) {
      return TileObjects.getFirstAt(
          new WorldPoint(3615, 9510, 0),
          Predicates.ids(Constants.ACTIVE_CELL_TILE_IDS)
      );
    }

    return null;
  }
}
