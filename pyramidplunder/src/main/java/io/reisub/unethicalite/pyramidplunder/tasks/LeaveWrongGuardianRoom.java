package io.reisub.unethicalite.pyramidplunder.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.NPCs;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.movement.Reachable;
import io.reisub.unethicalite.pyramidplunder.PyramidPlunder;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.TileObject;

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
  public void execute() {
    final TileObject exit = TileObjects.getNearest("Tomb Door");
    if (exit == null) {
      return;
    }

    exit.interact(0);
    Time.sleepTicksUntil(() -> Utils.isInRegion(PyramidPlunder.SOPHANEM_REGION), 10);
    Time.sleepTick();
  }
}
