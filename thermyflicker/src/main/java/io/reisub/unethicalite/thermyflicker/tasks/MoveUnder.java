package io.reisub.unethicalite.thermyflicker.tasks;

import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.coords.RectangularArea;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.widgets.Prayers;

public class MoveUnder extends Task {

  private NPC thermy;

  @Override
  public String getStatus() {
    return "Moving";
  }

  @Override
  public boolean validate() {
    thermy = NPCs.getNearest(NpcID.THERMONUCLEAR_SMOKE_DEVIL);

    return thermy != null
        && !thermy.isDead()
        && thermy.getInteracting().equals(Players.getLocal());
  }

  @Override
  public void execute() {
    final RectangularArea area = new RectangularArea(thermy.getWorldLocation().dx(1).dy(1), 1, 1);
    final WorldPoint nearest = area.getNearest();

    Movement.walk(nearest);
    Time.sleepTicks(2);

    if (Prayers.getPoints() == 0) {
      final Item prayerPotion = Inventory.getFirst(
          Predicates.ids(Constants.PRAYER_RESTORE_POTION_IDS));

      if (prayerPotion != null) {
        prayerPotion.interact("Drink");
      }
    }

    Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().equals(nearest), 3);
  }
}
