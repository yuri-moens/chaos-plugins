package io.reisub.unethicalite.tempoross.tasks;

import io.reisub.unethicalite.tempoross.Tempoross;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;

public class LeaveBoat extends Task {
  @Inject private Tempoross plugin;

  @Override
  public String getStatus() {
    return "Leaving boat";
  }

  @Override
  public boolean validate() {
    return plugin.isOnBoat() && plugin.getPlayersReady() > 1;
  }

  @Override
  public int execute() {
    TileObject ladder = TileObjects.getNearest(ObjectID.ROPE_LADDER_41305);
    if (ladder == null) {
      return 1;
    }

    ladder.interact("Quick-climb");
    Time.sleepUntil(() -> plugin.isInDesert(), 10000);
    return 1;
  }
}
