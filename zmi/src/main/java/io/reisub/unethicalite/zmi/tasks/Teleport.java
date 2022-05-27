package io.reisub.unethicalite.zmi.tasks;

import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import io.reisub.unethicalite.zmi.Zmi;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.magic.Magic;
import net.unethicalite.api.magic.SpellBook;

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
