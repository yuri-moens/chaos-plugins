package io.reisub.unethicalite.tempoross.tasks;

import io.reisub.unethicalite.tempoross.Tempoross;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Rand;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.widgets.Dialog;

public class EnterBoat extends Task {
  @Inject private Tempoross plugin;

  @Override
  public String getStatus() {
    return "Entering boat";
  }

  @Override
  public boolean validate() {
    return plugin.isInDesert() && (plugin.getPlayersReady() == 0 || Dialog.canContinueNPC());
  }

  @Override
  public int execute() {
    TileObject ladder = TileObjects.getNearest(ObjectID.ROPE_LADDER_41305);
    if (ladder == null) {
      return 1;
    }

    ladder.interact(0);
    Time.sleepTicks(Rand.nextInt(3, 5));
    Time.sleepUntil(() -> plugin.isOnBoat() || plugin.getPlayersReady() >= 1, 10000);

    if (!plugin.isOnBoat() && plugin.getPlayersReady() >= 1) {
      Movement.walk(new WorldPoint(3137, 2840, 0));
      Time.sleep(400, 700);
    }
    return 1;
  }
}
