package io.reisub.unethicalite.zmi.tasks;

import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import io.reisub.unethicalite.zmi.Zmi;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.client.Static;

public class EmptyPouch extends Task {
  @Override
  public String getStatus() {
    return "Emptying pouches";
  }

  @Override
  public boolean validate() {
    return !Zmi.pouchesAreEmpty
        && !Inventory.contains(Predicates.ids(Constants.ESSENCE_IDS))
        && Players.getLocal().distanceTo(Zmi.NEAR_ALTAR) < 5;
  }

  @Override
  public void execute() {
    Inventory.getAll(Predicates.ids(Constants.ESSENCE_POUCH_IDS))
        .forEach((i) -> i.interact("Empty"));

    if (Static.getClient().getTickCount() > Zmi.lastEmpty + 10) {
      Time.sleepTicksUntil(Inventory::isFull, 3);
    } else {
      if (!Time.sleepTicksUntil(
          () -> Inventory.contains(Predicates.ids(Constants.ESSENCE_IDS)), 3)) {
        Zmi.pouchesAreEmpty = true;
      }
    }

    Zmi.lastEmpty = Static.getClient().getTickCount();
  }
}
