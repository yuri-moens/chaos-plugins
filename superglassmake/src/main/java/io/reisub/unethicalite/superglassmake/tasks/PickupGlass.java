package io.reisub.unethicalite.superglassmake.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileItems;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.managers.Static;
import io.reisub.unethicalite.superglassmake.Config;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;

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
