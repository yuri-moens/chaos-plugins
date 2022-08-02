package io.reisub.unethicalite.thermyflicker.tasks;

import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Prayers;

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
  public int execute() {
    Inventory.getFirst(Predicates.ids(Constants.PRAYER_RESTORE_POTION_IDS)).interact("Drink");
    Time.sleepTicksUntil(() -> Prayers.getPoints() != 0, 5);
    return 1;
  }
}
