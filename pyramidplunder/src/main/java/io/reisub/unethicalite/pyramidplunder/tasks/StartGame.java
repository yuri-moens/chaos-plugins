package io.reisub.unethicalite.pyramidplunder.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.NPCs;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.movement.Reachable;
import io.reisub.unethicalite.pyramidplunder.PyramidPlunder;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;

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
