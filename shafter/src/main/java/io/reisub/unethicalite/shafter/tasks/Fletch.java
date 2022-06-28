package io.reisub.unethicalite.shafter.tasks;

import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Production;

public class Fletch extends Task {

  @Override
  public String getStatus() {
    return "Fletching";
  }

  @Override
  public boolean validate() {
    return Inventory.isFull();
  }

  @Override
  public void execute() {
    final Item knife = Inventory.getFirst(ItemID.KNIFE);
    final Item logs = Inventory.getFirst(Predicates.nameContains("logs"));

    if (knife == null || logs == null) {
      return;
    }

    knife.useOn(logs);

    Time.sleepTicksUntil(Production::isOpen, 5);

    Production.chooseOption(1);
    Time.sleepTicksUntil(() -> !Inventory.contains(Predicates.nameContains("logs")), 100);
  }
}
