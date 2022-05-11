package io.reisub.unethicalite.pyramidplunder.tasks;

import dev.unethicalite.api.commons.Predicates;
import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.game.Vars;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.VarPlayer;

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
  public void execute() {
    Inventory.getFirst(Predicates.ids(Constants.ANTI_POISON_POTION_IDS)).interact("Drink");
    Time.sleepTicksUntil(() -> Vars.getVarp(VarPlayer.POISON.getId()) == 0, 3);
  }
}
