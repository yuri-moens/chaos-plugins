package io.reisub.unethicalite.guardiansoftheriftsolo.tasks;

import io.reisub.unethicalite.guardiansoftheriftsolo.GuardiansOfTheRiftSolo;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class DepositRunes extends Task {

  @Inject
  private GuardiansOfTheRiftSolo plugin;

  @Override
  public String getStatus() {
    return "Depositing runes";
  }

  @Override
  public boolean validate() {
    return Inventory.contains(Predicates.ids(Constants.RUNE_IDS))
        && plugin.isNearStart();
  }

  @Override
  public void execute() {
    final TileObject depositPool = TileObjects.getNearest(ObjectID.DEPOSIT_POOL);

    if (depositPool == null) {
      return;
    }

    depositPool.interact("Deposit-runes");
    Time.sleepTicksUntil(() -> !Inventory.contains(Predicates.ids(Constants.RUNE_IDS)), 20);
  }
}
