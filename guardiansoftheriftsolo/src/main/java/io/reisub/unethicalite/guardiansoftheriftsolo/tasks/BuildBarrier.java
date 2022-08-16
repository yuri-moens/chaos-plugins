package io.reisub.unethicalite.guardiansoftheriftsolo.tasks;

import io.reisub.unethicalite.guardiansoftheriftsolo.GuardiansOfTheRiftSolo;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.AreaType;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class BuildBarrier extends Task {

  private static final List<WorldPoint> cellTilePoints = new ArrayList<>(3) {
    {
      add(new WorldPoint(3619, 9509, 0));
      add(new WorldPoint(3615, 9510, 0));
      add(new WorldPoint(3611, 9509, 0));
    }
  };

  @Inject
  private GuardiansOfTheRiftSolo plugin;

  @Override
  public String getStatus() {
    return "Building barrier";
  }

  @Override
  public boolean validate() {
    return plugin.isGameActive()
        && AreaType.getCurrent() == AreaType.MAIN
        && Inventory.contains(Predicates.ids(Constants.CELL_IDS))
        && getCellTile() != null;
  }

  @Override
  public void execute() {
    final TileObject cellTile = getCellTile();

    if (cellTile == null) {
      return;
    }

    cellTile.interact("Place-cell");
    Time.sleepTicksUntil(
        () -> TileObjects.getFirstAt(cellTile.getWorldLocation(), cellTile.getId()) == null,
        30);
  }

  private TileObject getCellTile() {
    for (WorldPoint point : cellTilePoints) {
      final TileObject cellTile =
          TileObjects.getFirstAt(point, Predicates.ids(Constants.INACTIVE_CELL_TILE_IDS));

      if (cellTile != null) {
        return cellTile;
      }
    }

    return null;
  }
}
