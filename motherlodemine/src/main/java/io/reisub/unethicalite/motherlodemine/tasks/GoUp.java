package io.reisub.unethicalite.motherlodemine.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.motherlodemine.Config;
import io.reisub.unethicalite.motherlodemine.MotherlodeMine;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;

public class GoUp extends Task {
  @Inject
  private MotherlodeMine plugin;

  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Going up";
  }

  @Override
  public boolean validate() {
    return config.upstairs()
        && plugin.getCurrentActivity() == Activity.IDLE
        && !Inventory.contains(ItemID.PAYDIRT)
        && !plugin.isUpstairs();
  }

  @Override
  public void execute() {
    TileObject ladder = TileObjects.getNearest(o -> o.getName().equals("Ladder") && o.hasAction("Climb"));
    if (ladder == null) {
      return;
    }

    GameThread.invoke(() -> ladder.interact("Climb"));
    Time.sleepTicksUntil(() -> plugin.isUpstairs(), 20);
    Time.sleepTick();
  }
}
