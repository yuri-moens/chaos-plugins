package io.reisub.unethicalite.superglassmake.tasks;

import io.reisub.unethicalite.superglassmake.Config;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileItems;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.client.Static;

public class PickupGlass extends Task {
  @Inject private Config config;

  private int last;

  @Override
  public String getStatus() {
    return "Picking up glass";
  }

  @Override
  public boolean validate() {
    return config.pickupGlass()
        && !Inventory.isFull()
        && last + 2 < Static.getClient().getTickCount()
        && TileItems.getAt(Players.getLocal().getWorldLocation(), ItemID.MOLTEN_GLASS).size() >= 27;
  }

  @Override
  public void execute() {
    TileItems.getAt(Players.getLocal().getWorldLocation(), ItemID.MOLTEN_GLASS)
        .subList(0, 27)
        .forEach(
            (i) -> {
              i.interact("Take");
              Time.sleepTick();
            });

    last = Static.getClient().getTickCount();
  }
}
