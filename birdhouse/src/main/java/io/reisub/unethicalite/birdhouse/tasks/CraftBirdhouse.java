package io.reisub.unethicalite.birdhouse.tasks;

import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Inventory;

public class CraftBirdhouse extends Task {

  @Override
  public String getStatus() {
    return "Crafting bird house";
  }

  @Override
  public boolean validate() {
    return Inventory.contains(ItemID.CLOCKWORK);
  }

  @Override
  public void execute() {
    final Item chisel = Inventory.getFirst(ItemID.CHISEL);
    final Item logs = Inventory.getFirst((i) -> Constants.LOG_IDS.contains(i.getId()));

    if (chisel == null || logs == null) {
      return;
    }

    chisel.useOn(logs);
    Time.sleepTicksUntil(() -> !Inventory.contains(ItemID.CLOCKWORK), 5);
  }
}
