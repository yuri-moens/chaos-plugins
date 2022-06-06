package io.reisub.unethicalite.sulliuscep.tasks;

import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.client.Static;

public class Eat extends Task {

  private int last;

  @Override
  public String getStatus() {
    return "Eating";
  }

  @Override
  public boolean validate() {
    return Static.getClient().getTickCount() - last > 3
        && Combat.getCurrentHealth() < 20
        && Inventory.contains(Predicates.ids(Constants.BREW_POTION_IDS));
  }

  @Override
  public void execute() {
    final Item brew = Inventory.getFirst(Predicates.ids(Constants.BREW_POTION_IDS));

    if (brew == null) {
      return;
    }

    brew.interact("Drink");
    last = Static.getClient().getTickCount();
  }
}
