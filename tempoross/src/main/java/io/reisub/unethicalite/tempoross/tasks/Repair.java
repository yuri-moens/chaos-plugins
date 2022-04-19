package io.reisub.unethicalite.tempoross.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileObjects;
import io.reisub.unethicalite.tempoross.Tempoross;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.NullObjectID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;

public class Repair extends Task {
  TileObject brokenObject;
  @Inject private Tempoross plugin;

  @Override
  public String getStatus() {
    return "Repairing";
  }

  @Override
  public boolean validate() {
    if (!plugin.isInTemporossArea()
        || !plugin.isWaveIncoming()
        || plugin.getCurrentActivity() == Activity.REPAIRING) {
      return false;
    }

    TileObject tetherObject =
        TileObjects.getNearest(
            NullObjectID.NULL_41352,
            NullObjectID.NULL_41353,
            NullObjectID.NULL_41354,
            NullObjectID.NULL_41355);
    brokenObject =
        TileObjects.getNearest(
            ObjectID.DAMAGED_MAST_40996,
            ObjectID.DAMAGED_MAST_40997,
            ObjectID.DAMAGED_TOTEM_POLE,
            ObjectID.DAMAGED_TOTEM_POLE_41011);

    if (brokenObject == null) {
      return false;
    }

    if (tetherObject == null) {
      return true;
    }

    return Players.getLocal().distanceTo(tetherObject)
        > Players.getLocal().distanceTo(brokenObject);
  }

  @Override
  public void execute() {
    brokenObject.interact(0);
    if (!Time.sleepTicksUntil(() -> plugin.getCurrentActivity() == Activity.IDLE, 3)) {
      return;
    }

    Time.sleepUntil(() -> plugin.getCurrentActivity() == Activity.REPAIRING, 15000);
  }
}
