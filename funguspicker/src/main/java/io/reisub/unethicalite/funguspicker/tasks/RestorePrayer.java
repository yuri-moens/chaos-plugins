package io.reisub.unethicalite.funguspicker.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.Skills;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.funguspicker.FungusPicker;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Interact;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ObjectID;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;

public class RestorePrayer extends Task {
  @Inject
  private FungusPicker plugin;

  @Override
  public String getStatus() {
    return "Restoring prayer points";
  }

  @Override
  public boolean validate() {
    return Skills.getBoostedLevel(Skill.PRAYER) < Skills.getLevel(Skill.PRAYER)
        && (Inventory.isFull() || Skills.getBoostedLevel(Skill.PRAYER) == 0);
  }

  @Override
  public void execute() {
    WorldPoint current = Players.getLocal().getWorldLocation();

    boolean interacted = Interact.interactWithInventoryOrEquipment(Predicates.ids(Constants.ARDOUGNE_CLOAK_IDS), "Monastery Teleport", "Kandarin Monastery", 0);

    if (!interacted) {
      interacted = Interact.interactWithInventoryOrEquipment(Predicates.ids(Constants.DUELING_RING_IDS), "Rub", "Ferox Enclave", 3);

      if (!interacted) {
        plugin.stop("No dueling ring or Ardougne cloak found. Stopping script.");
        return;
      }
    }

    Time.sleepTicksUntil(() -> !Players.getLocal().getWorldLocation().equals(current), 10);
    Time.sleepTick();

    if (Players.getLocal().getWorldLocation().getRegionID() == FungusPicker.MONASTERY_REGION) {
      TileObjects.getNearest(ObjectID.ALTAR).interact(0);
    } else {
      TileObjects.getNearest(ObjectID.POOL_OF_REFRESHMENT).interact(0);
    }

    Time.sleepTicksUntil(() -> Skills.getBoostedLevel(Skill.PRAYER) == Skills.getLevel(Skill.PRAYER), 30);
    Time.sleepTicks(3);
  }
}
