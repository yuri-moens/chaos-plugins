package io.reisub.unethicalite.motherlodemine.tasks;

import io.reisub.unethicalite.motherlodemine.MotherlodeMine;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;

public class FixWheel extends Task {
  @Inject private MotherlodeMine plugin;

  @Override
  public String getStatus() {
    return "Fixing wheel";
  }

  @Override
  public boolean validate() {
    return plugin.getCurrentActivity() == Activity.IDLE
        && !plugin.isUpstairs()
        && plugin.getPreviousActivity() == Activity.DEPOSITING
        && TileObjects.getAll(ObjectID.BROKEN_STRUT).size() == 2;
  }

  @Override
  public int execute() {
    TileObject strut = TileObjects.getNearest(ObjectID.BROKEN_STRUT);
    if (strut == null) {
      return 1;
    }

    plugin.setActivity(Activity.REPAIRING);

    GameThread.invoke(() -> strut.interact("Hammer"));
    Time.sleepTicksUntil(() -> plugin.getCurrentActivity() == Activity.IDLE, 30);
    return 1;
  }
}
