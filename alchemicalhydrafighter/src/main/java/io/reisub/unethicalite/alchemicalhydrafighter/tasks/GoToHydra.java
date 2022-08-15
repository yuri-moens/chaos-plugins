package io.reisub.unethicalite.alchemicalhydrafighter.tasks;

import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.game.Skills;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.client.Static;

public class GoToHydra extends Task {

  @Override
  public String getStatus() {
    return "Going to Hydra";
  }

  @Override
  public boolean validate() {
    return !Static.getClient().isInInstancedRegion()
        && !Utils.isInRegion(5536);
  }

  @Override
  public void execute() {
    if (Combat.getMissingHealth() > 0
        || Skills.getLevel(Skill.PRAYER) - Skills.getBoostedLevel(Skill.PRAYER) > 0) {
      ChaosMovement.teleportToHouse();
      ChaosMovement.drinkFromPool(101);
    }

    Movement.walkTo(1352, 10259, 0);

    final TileObject door = TileObjects.getNearest(0);

    if (door == null) {
      return;
    }

    door.interact("Enter");
  }
}
