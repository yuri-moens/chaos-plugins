package io.reisub.unethicalite.guardiansoftheriftsolo.tasks;

import io.reisub.unethicalite.guardiansoftheriftsolo.GuardiansOfTheRiftSolo;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.AreaType;
import io.reisub.unethicalite.utils.api.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class LeaveLargeRemains extends Task {

  private static final WorldPoint END_OF_RUBBLE = new WorldPoint(3633, 9503, 0);

  @Inject
  private GuardiansOfTheRiftSolo plugin;

  @Override
  public String getStatus() {
    return "Leaving large remains";
  }

  @Override
  public boolean validate() {
    return AreaType.getCurrent() == AreaType.LARGE_REMAINS
        && (Inventory.getCount(true, ItemID.GUARDIAN_FRAGMENTS) >= 80
        || plugin.getElapsedTicks() >= 100);
  }

  @Override
  public void execute() {
    plugin.setActivity(Activity.IDLE);

    final TileObject rubble = TileObjects.getNearest(ObjectID.RUBBLE_43726);

    if (rubble == null) {
      return;
    }

    rubble.interact("Climb");
    Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().equals(END_OF_RUBBLE), 20);
    Time.sleepTicks(4);
  }
}
