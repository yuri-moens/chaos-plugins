package io.reisub.unethicalite.thermyflicker.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.coords.RectangularArea;
import dev.unethicalite.api.entities.NPCs;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.movement.Movement;
import dev.unethicalite.api.widgets.Prayers;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.coords.WorldPoint;

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
