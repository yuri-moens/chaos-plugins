package io.reisub.unethicalite.mta.tasks;

import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.client.Static;

public class EatPeach extends Task {
  private int last;

  @Override
  public String getStatus() {
    return "Eating a peach";
  }

  @Override
  public boolean validate() {
    return Inventory.contains(ItemID.PEACH)
        && Combat.getMissingHealth() >= 8
        && Static.getClient().getTickCount() > last + 3;
  }

  @Override
  public void execute() {
    final Item peach = Inventory.getFirst(ItemID.PEACH);
    if (peach == null) {
      return;
    }

    peach.interact(1);
    last = Static.getClient().getTickCount();
  }
}
