package io.reisub.unethicalite.pickpocket.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileItems;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ItemID;
import net.runelite.api.TileItem;
import net.runelite.api.coords.WorldPoint;

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
  public void execute() {
    int count = Inventory.getCount(ItemID.JUG_OF_WINE);

    Time.sleepTick();
    wine.interact("Take");
    Time.sleepTicksUntil(() -> Inventory.getCount(ItemID.JUG_OF_WINE) > count, 10);
  }
}
