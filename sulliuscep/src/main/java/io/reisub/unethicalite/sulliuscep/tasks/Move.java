package io.reisub.unethicalite.sulliuscep.tasks;

import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.movement.Movement;

public class Move extends Task {

  @Override
  public String getStatus() {
    return "Moving out of attack range";
  }

  @Override
  public boolean validate() {
    final NPC tarMonster = NPCs.getNearest(NpcID.TAR_MONSTER);

    if (tarMonster == null) {
      return false;
    }

    return Players.getLocal().getWorldLocation().equals(new WorldPoint(3684, 3774, 0))
        && tarMonster.getInteracting() != null
        && tarMonster.getInteracting().equals(Players.getLocal())
        && tarMonster.getWorldLocation().equals(new WorldPoint(3681, 3764, 0));
  }

  @Override
  public void execute() {
    final WorldPoint destination = new WorldPoint(3682, 3775, 0);

    Movement.walk(destination);

    Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().equals(destination), 10);
  }
}
