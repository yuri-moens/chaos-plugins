package io.reisub.unethicalite.motherlodemine.tasks;

import io.reisub.unethicalite.motherlodemine.MotherlodeMine;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.NullObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;

public class GoDown extends Task {

  @Inject
  private MotherlodeMine plugin;

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
    final TileObject ladder = TileObjects.getNearest(NullObjectID.NULL_19045);

    if (ladder == null || !ladder.hasAction("Climb")) {
      Movement.walk(new WorldPoint(3755, 5675, 0));

      Time.sleepTicksUntil(() -> {
        final TileObject ladder2 = TileObjects.getNearest(NullObjectID.NULL_19045);

        return ladder2 != null && ladder2.hasAction("Climb");
      }, 10);

      return;
    }

    GameThread.invoke(() -> ladder.interact("Climb"));
    Time.sleepTicksUntil(() -> !plugin.isUpstairs(), 20);
    Time.sleepTick();
  }
}
