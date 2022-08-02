package io.reisub.unethicalite.pickpocket.tasks;

import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ItemID;
import net.runelite.api.TileItem;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileItems;
import net.unethicalite.api.items.Inventory;

public class TakeWine extends Task {
  private TileItem wine;

  @Override
  public String getStatus() {
    return "Taking jug of wine";
  }

  @Override
  public boolean validate() {
    return Inventory.getFreeSlots() >= 4
        && Players.getLocal().getModelHeight() != 1000
        && (wine = TileItems.getFirstAt(new WorldPoint(3663, 3380, 0), ItemID.JUG_OF_WINE)) != null;
  }

  @Override
  public int execute() {
    int count = Inventory.getCount(ItemID.JUG_OF_WINE);

    Time.sleepTick();
    wine.interact("Take");
    Time.sleepTicksUntil(() -> Inventory.getCount(ItemID.JUG_OF_WINE) > count, 10);

    return 1;
  }
}
