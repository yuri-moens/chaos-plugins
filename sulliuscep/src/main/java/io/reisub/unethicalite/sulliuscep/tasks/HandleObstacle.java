package io.reisub.unethicalite.sulliuscep.tasks;

import com.google.common.collect.ImmutableSet;
import io.reisub.unethicalite.sulliuscep.Sulliuscep;
import io.reisub.unethicalite.sulliuscep.SulliuscepObject;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;

public class HandleObstacle extends Task {

  private static final ImmutableSet<Integer> OBSTACLE_IDS = ImmutableSet.of(
      ObjectID.VINES_30644,
      ObjectID.THICK_VINE,
      ObjectID.THICK_VINES
  );

  @Inject
  private Sulliuscep plugin;
  @Inject
  private CurePoison curePoisonTask;
  @Inject
  private Eat eatTask;
  private TileObject obstacle;

  @Override
  public String getStatus() {
    return "Handling obstacle";
  }

  @Override
  public boolean validate() {
    final SulliuscepObject sulliuscep = plugin.getCurrentSulliuscep();

    if (sulliuscep == SulliuscepObject.SULLIUSCEP_1
        || sulliuscep.getObstacles() == null
        || sulliuscep.isReachable()) {
      return false;
    }

    obstacle = null;

    for (WorldPoint obstaclePoint : sulliuscep.getObstacles()) {
      obstacle = TileObjects.getFirstAt(obstaclePoint, Predicates.ids(OBSTACLE_IDS));

      if (obstacle != null) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void execute() {
    final WorldPoint worldPoint = obstacle.getWorldLocation();

    GameThread.invoke(() -> obstacle.interact(0));
    Time.sleepTick();

    switch (plugin.getCurrentSulliuscep()) {
      case SULLIUSCEP_2:
      case SULLIUSCEP_3:
        Inventory.getAll(ItemID.MORT_MYRE_FUNGUS, ItemID.MUSHROOM)
            .forEach(i -> i.interact("Drop"));
        Time.sleepTick();

        GameThread.invoke(() -> obstacle.interact(0));
        break;
      default:
    }

    Time.sleepTicksUntil(
        () -> TileObjects.getFirstAt(worldPoint, Predicates.ids(OBSTACLE_IDS)) == null
            || Combat.isPoisoned()
            || Combat.getCurrentHealth() < 30, 30);

    if (Combat.isPoisoned()) {
      curePoisonTask.execute();
      Time.sleepTick();

      GameThread.invoke(() -> obstacle.interact(0));

      Time.sleepTicksUntil(
          () -> TileObjects.getFirstAt(worldPoint, Predicates.ids(OBSTACLE_IDS)) == null, 30);
    }

    if (Combat.getCurrentHealth() < 30) {
      eatTask.execute();
      Time.sleepTick();

      GameThread.invoke(() -> obstacle.interact(0));

      Time.sleepTicksUntil(
          () -> TileObjects.getFirstAt(worldPoint, Predicates.ids(OBSTACLE_IDS)) == null, 30);
    }
  }
}
