package io.reisub.unethicalite.motherlodemine.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.motherlodemine.MotherlodeMine;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.TileObject;

public class GoDown extends Task {
  @Inject private MotherlodeMine plugin;

  @Override
  public String getStatus() {
    return "Going down";
  }

  @Override
  public boolean validate() {
    return plugin.getCurrentActivity() == Activity.IDLE
        && plugin.isUpstairs()
        && Inventory.isFull();
  }

  @Override
  public void execute() {
    TileObject ladder =
        TileObjects.getNearest(o -> o.getName().equals("Ladder") && o.hasAction("Climb"));
    if (ladder == null) {
      return;
    }

    GameThread.invoke(() -> ladder.interact("Climb"));
    Time.sleepTicksUntil(() -> !plugin.isUpstairs(), 20);
    Time.sleepTick();
  }
}
