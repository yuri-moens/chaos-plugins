package io.reisub.unethicalite.funguspicker.tasks;

import io.reisub.unethicalite.funguspicker.FungusPicker;
import io.reisub.unethicalite.utils.api.Interact;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ObjectID;
import net.runelite.api.Skill;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.Skills;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;

public class CastBloom extends Task {
  @Inject private FungusPicker plugin;

  @Override
  public String getStatus() {
    return "Casting bloom";
  }

  @Override
  public boolean validate() {
    return !Inventory.isFull()
        && TileObjects.getNearest(ObjectID.FUNGI_ON_LOG) == null
        && Skills.getBoostedLevel(Skill.PRAYER) > 0;
  }

  @Override
  public int execute() {
    if (!Players.getLocal().getWorldLocation().equals(FungusPicker.FUNGUS_LOCATION)) {
      Movement.walk(FungusPicker.FUNGUS_LOCATION);

      Time.sleepTicksUntil(
          () -> Players.getLocal().getWorldLocation().equals(FungusPicker.FUNGUS_LOCATION), 10);
    }

    boolean interacted =
        Interact.interactWithInventoryOrEquipment((i) -> i.hasAction("Bloom"), "Bloom", null, 0);
    if (!interacted) {
      plugin.stop("Couldn't find an item to cast Bloom with. Stopping plugin.");
      return 1;
    }

    return 4;
  }
}
