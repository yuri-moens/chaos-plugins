package io.reisub.unethicalite.alchemicalhydrafighter.tasks;

import io.reisub.unethicalite.alchemicalhydrafighter.Config;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.items.Inventory;

public class GoToBank extends Task {

  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Going to bank";
  }

  @Override
  public boolean validate() {
    return (!haveFood() && Combat.getCurrentHealth() < 40)
        || Inventory.isFull();
  }

  @Override
  public void execute() {
    config.teleportLocation().teleport(false, true);
  }

  private boolean haveFood() {
    return Inventory.contains(Predicates.ids(Constants.BREW_POTION_IDS))
        || Inventory.contains(i -> i.hasAction("Eat"));
  }
}
