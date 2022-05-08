package io.reisub.unethicalite.thermyflicker.tasks;

import dev.unethicalite.api.commons.Predicates;
import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.NPCs;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.widgets.Prayers;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;

public class DrinkPrayerPotion extends Task {

  @Override
  public String getStatus() {
    return "Drinking prayer potion";
  }

  @Override
  public boolean validate() {
    final NPC thermy = NPCs.getNearest(NpcID.THERMONUCLEAR_SMOKE_DEVIL);
    return (thermy == null || thermy.isDead())
        && Prayers.getPoints() == 0
        && Inventory.contains(Predicates.ids(Constants.PRAYER_RESTORE_POTION_IDS));
  }

  @Override
  public void execute() {
    Inventory.getFirst(Predicates.ids(Constants.PRAYER_RESTORE_POTION_IDS)).interact("Drink");
    Time.sleepTicksUntil(() -> Prayers.getPoints() != 0, 5);
  }
}
