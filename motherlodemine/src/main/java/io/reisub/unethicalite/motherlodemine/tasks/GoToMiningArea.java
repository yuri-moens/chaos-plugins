package io.reisub.unethicalite.motherlodemine.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.movement.Movement;
import io.reisub.unethicalite.motherlodemine.Config;
import io.reisub.unethicalite.motherlodemine.MotherlodeMine;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ObjectID;
import net.runelite.api.coords.WorldPoint;

public class GoToMiningArea extends Task {

  @Inject
  private MotherlodeMine plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Going to mining area";
  }

  @Override
  public boolean validate() {
    if (plugin.getMiningArea().getMiningArea().contains(Players.getLocal())) {
      return false;
    }

    final int playerX = Players.getLocal().getWorldLocation().getX();
    final int playerY = Players.getLocal().getWorldLocation().getY();

    switch (plugin.getMiningArea()) {
      case BEHIND_SHORTCUT:
        return playerX > 3764
            && playerY > 5669
            && TileObjects.getFirstAt(3766, 5670, 0, ObjectID.ROCKFALL) != null;
      case NORTH:
        return playerY < 5684;
      default:
        return false;
    }
  }

  @Override
  public void execute() {
    switch (plugin.getMiningArea()) {
      case BEHIND_SHORTCUT:
        plugin.mineRockfall(3766, 5670);
        break;
      case NORTH:
        plugin.mineRockfall(3733, 5680);
        plugin.mineRockfall(3731, 5683);

        Movement.walk(new WorldPoint(3732, 5688, 0));

        Time.sleepTicksUntil(() -> Players.getLocal().isMoving(), 3);
        Time.sleepTicksUntil(() -> !Players.getLocal().isMoving()
            || Players.getLocal().getWorldLocation().getY() > 5683, 30);
        break;
      default:
        break;
    }
  }
}
