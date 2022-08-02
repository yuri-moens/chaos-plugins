package io.reisub.unethicalite.pyramidplunder.tasks;

import io.reisub.unethicalite.pyramidplunder.PyramidPlunder;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.movement.Reachable;

public class LeaveWrongGuardianRoom extends Task {

  @Override
  public String getStatus() {
    return "Leaving wrong guardian room";
  }

  @Override
  public boolean validate() {
    NPC mummy;

    return Utils.isInRegion(PyramidPlunder.PYRAMID_PLUNDER_REGION)
        && Players.getLocal().getWorldLocation().getPlane() != 0
        && ((mummy = NPCs.getNearest(NpcID.GUARDIAN_MUMMY)) == null
        || !Reachable.isInteractable(mummy));
  }

  @Override
  public int execute() {
    final TileObject exit = TileObjects.getNearest("Tomb Door");
    if (exit == null) {
      return 1;
    }

    exit.interact(0);
    Time.sleepTicksUntil(() -> Utils.isInRegion(PyramidPlunder.SOPHANEM_REGION), 10);
    return 2;
  }
}
