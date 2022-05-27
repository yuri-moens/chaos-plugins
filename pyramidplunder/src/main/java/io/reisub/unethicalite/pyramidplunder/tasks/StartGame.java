package io.reisub.unethicalite.pyramidplunder.tasks;

import io.reisub.unethicalite.pyramidplunder.PyramidPlunder;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.movement.Reachable;

public class StartGame extends Task {

  @Inject
  private PyramidPlunder plugin;

  private NPC mummy;

  @Override
  public String getStatus() {
    return "Starting game";
  }

  @Override
  public boolean validate() {
    return Utils.isInRegion(PyramidPlunder.PYRAMID_PLUNDER_REGION)
        && (mummy = NPCs.getNearest(NpcID.GUARDIAN_MUMMY)) != null
        && Reachable.isInteractable(mummy);
  }

  @Override
  public void execute() {
    GameThread.invoke(() -> mummy.interact("Start-minigame"));
    Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().getPlane() == 0, 20);
  }
}
