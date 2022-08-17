package io.reisub.unethicalite.guardiansoftheriftsolo.tasks;

import io.reisub.unethicalite.guardiansoftheriftsolo.GuardiansOfTheRiftSolo;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.AreaType;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class LeaveAltar extends Task {

  @Inject
  private GuardiansOfTheRiftSolo plugin;

  @Override
  public String getStatus() {
    return "Leaving altar";
  }

  @Override
  public boolean validate() {
    return AreaType.getCurrent() == AreaType.ALTAR
        && !Inventory.contains(ItemID.GUARDIAN_ESSENCE)
        && Inventory.contains(Predicates.ids(Constants.CELL_IDS));
  }

  @Override
  public void execute() {
    final TileObject portal = TileObjects.getNearest("Portal");

    if (portal == null) {
      return;
    }

    portal.interact("Use");
    Time.sleepTicksUntil(() -> AreaType.getCurrent() == AreaType.MAIN, 20);
  }
}
