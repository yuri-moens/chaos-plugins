package io.reisub.unethicalite.mta.tasks;

import dev.unethicalite.api.game.Combat;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.client.Static;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.ItemID;

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
