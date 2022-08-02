package io.reisub.unethicalite.pyramidplunder.tasks;

import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.VarPlayer;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.game.Vars;
import net.unethicalite.api.items.Inventory;

public class DrinkAntiPoison extends Task {

  @Override
  public String getStatus() {
    return "Drinking anti poison";
  }

  @Override
  public boolean validate() {
    return Vars.getVarp(VarPlayer.POISON.getId()) > 0
        && Inventory.contains(Predicates.ids(Constants.ANTI_POISON_POTION_IDS));
  }

  @Override
  public int execute() {
    Inventory.getFirst(Predicates.ids(Constants.ANTI_POISON_POTION_IDS)).interact("Drink");
    Time.sleepTicksUntil(() -> Vars.getVarp(VarPlayer.POISON.getId()) == 0, 3);

    return 1;
  }
}
