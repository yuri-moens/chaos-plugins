package io.reisub.unethicalite.sulliuscep.tasks;

import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Prayers;

public class CurePoison extends Task {

  @Override
  public String getStatus() {
    return "Curing poison";
  }

  @Override
  public boolean validate() {
    return Combat.isPoisoned();
  }

  @Override
  public int execute() {
    final Item prayerBook = Inventory.getFirst(ItemID.PRAYER_BOOK_10890);

    if (prayerBook != null && Prayers.getPoints() >= 2) {
      prayerBook.interact("Recite-prayer");

      Time.sleepTicksUntil(() -> !Combat.isPoisoned(), 3);
      return 1;
    }

    return 1;
  }
}
