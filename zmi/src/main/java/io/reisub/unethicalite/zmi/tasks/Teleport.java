package io.reisub.unethicalite.zmi.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.magic.Magic;
import dev.unethicalite.api.magic.SpellBook;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import io.reisub.unethicalite.zmi.Zmi;

public class Teleport extends Task {
  @Override
  public String getStatus() {
    return "Teleporting";
  }

  @Override
  public boolean validate() {
    return !Inventory.contains(Predicates.ids(Constants.ESSENCE_IDS))
        && Players.getLocal().distanceTo(Zmi.NEAR_ALTAR) < 5
        && Zmi.pouchesAreEmpty;
  }

  @Override
  public void execute() {
    Magic.cast(SpellBook.Lunar.OURANIA_TELEPORT);
    Time.sleepTicksUntil(() -> Players.getLocal().distanceTo(Zmi.NEAR_ALTAR) > 5, 10);
    Time.sleepTick();
  }
}
