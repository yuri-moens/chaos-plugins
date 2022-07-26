package io.reisub.unethicalite.woodcutting.tasks;

import io.reisub.unethicalite.utils.tasks.Task;
import io.reisub.unethicalite.woodcutting.Config;
import io.reisub.unethicalite.woodcutting.Woodcutting;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.GameObject;
import net.runelite.api.coords.Direction;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.movement.Reachable;

public class MoveToRespawning extends Task {

  @Inject
  private Woodcutting plugin;
  @Inject
  private Config config;
  private final ArrayDeque<WorldPoint> despawnedTreeLocations = new ArrayDeque<>();

  @Override
  public String getStatus() {
    return "Moving to respawning tree";
  }

  @Override
  public boolean validate() {
    return !despawnedTreeLocations.isEmpty()
        && !Players.getLocal().isMoving()
        && noTreesAvailable()
        && Players.getLocal().distanceTo(despawnedTreeLocations.getFirst()) > 1;
  }

  @Override
  public void execute() {
    final WorldPoint treePosition = despawnedTreeLocations.getFirst();
    final List<WorldPoint> neighbours = new ArrayList<>();
    neighbours.add(Reachable.getNeighbour(Direction.NORTH, treePosition));
    neighbours.add(Reachable.getNeighbour(Direction.EAST, treePosition));
    neighbours.add(Reachable.getNeighbour(Direction.SOUTH, treePosition));
    neighbours.add(Reachable.getNeighbour(Direction.WEST, treePosition));

    WorldPoint nearest = null;

    for (WorldPoint neighbour : neighbours) {
      if (!Reachable.isWalkable(neighbour)) {
        continue;
      }

      if (nearest == null) {
        nearest = neighbour;
        continue;
      }

      if (Players.getLocal().distanceTo(nearest) > Players.getLocal().distanceTo(neighbour)) {
        nearest = neighbour;
      }
    }

    if (nearest == null) {
      return;
    }

    Movement.walk(nearest);
    Time.sleepTicksUntil(() -> Players.getLocal().isMoving(), 5);
  }

  @Subscribe
  private void onGameObjectSpawned(GameObjectSpawned event) {
    if (!plugin.isRunning() || event.getGameObject() == null) {
      return;
    }

    if (config.location().getTreeIds().contains(event.getGameObject().getId())) {
      despawnedTreeLocations.remove(event.getGameObject().getWorldLocation());
    }
  }

  @Subscribe
  private void onGameObjectDespawned(GameObjectDespawned event) {
    if (!plugin.isRunning() || event.getGameObject() == null) {
      return;
    }

    final GameObject o = event.getGameObject();

    if (config.location().getTreeIds().contains(o.getId())) {
      final ArrayDeque<WorldPoint> treePositions = config.location().getTreePositions();

      if (treePositions != null && !treePositions.isEmpty()) {
        for (WorldPoint treePosition : treePositions) {
          if (treePosition.equals(o.getWorldLocation())) {
            despawnedTreeLocations.add(o.getWorldLocation());
            break;
          }
        }
      } else {
        despawnedTreeLocations.add(o.getWorldLocation());
      }
    }
  }

  private boolean noTreesAvailable() {
    final ArrayDeque<WorldPoint> treePositions = config.location().getTreePositions();

    if (treePositions == null || treePositions.isEmpty()) {
      return TileObjects.getNearest(Predicates.ids(config.location().getTreeIds())) == null;
    } else {
      return TileObjects.getNearest(
          o -> config.location().getTreeIds().contains(o.getId())
              && config.location().getTreePositions().contains(o.getWorldLocation())) == null;
    }
  }
}
