package io.reisub.unethicalite.mining.tasks;

import io.reisub.unethicalite.mining.Config;
import io.reisub.unethicalite.mining.Mining;
import io.reisub.unethicalite.mining.RockPosition;
import io.reisub.unethicalite.utils.tasks.Task;
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
  @Inject private Mining plugin;
  @Inject private Config config;

  private final ArrayDeque<WorldPoint> despawnedRockLocations = new ArrayDeque<>();

  @Override
  public String getStatus() {
    return "Moving to respawning rock";
  }

  @Override
  public boolean validate() {
    return !despawnedRockLocations.isEmpty()
        && !Players.getLocal().isMoving()
        && !config.location().isThreeTick()
        && noRocksAvailable()
        && Players.getLocal().distanceTo(despawnedRockLocations.getFirst()) > 1;
  }

  @Override
  public void execute() {
    final WorldPoint rockLocation = despawnedRockLocations.getFirst();
    final List<WorldPoint> neighbours = new ArrayList<>();
    neighbours.add(Reachable.getNeighbour(Direction.NORTH, rockLocation));
    neighbours.add(Reachable.getNeighbour(Direction.EAST, rockLocation));
    neighbours.add(Reachable.getNeighbour(Direction.SOUTH, rockLocation));
    neighbours.add(Reachable.getNeighbour(Direction.WEST, rockLocation));

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

      Movement.walk(nearest);
      Time.sleepTicksUntil(() -> Players.getLocal().isMoving(), 5);
    }
  }

  @Subscribe
  private void onGameObjectDespawned(GameObjectDespawned event) {
    if (!plugin.isRunning() || event.getGameObject() == null) {
      return;
    }

    final GameObject o = event.getGameObject();

    if (config.location().getRockIds().contains(o.getId())) {
      final ArrayDeque<RockPosition> rockPositions = config.location().getRockPositions();

      if (rockPositions != null && !rockPositions.isEmpty()) {
        for (RockPosition rockPosition : rockPositions) {
          if (rockPosition.getRock().equals(o.getWorldLocation())) {
            despawnedRockLocations.add(o.getWorldLocation());
            break;
          }
        }
      } else {
        despawnedRockLocations.add(o.getWorldLocation());
      }
    }
  }

  @Subscribe
  private void onGameObjectSpawned(GameObjectSpawned event) {
    if (!plugin.isRunning() || event.getGameObject() == null) {
      return;
    }

    if (config.location().getRockIds().contains(event.getGameObject().getId())) {
      despawnedRockLocations.remove(event.getGameObject().getWorldLocation());
    }
  }

  private boolean noRocksAvailable() {
    final ArrayDeque<RockPosition> rockPositions = config.location().getRockPositions();

    if (rockPositions == null || rockPositions.isEmpty()) {
      return TileObjects.getNearest(Predicates.ids(config.location().getRockIds())) == null;
    } else {
      return TileObjects.getNearest(
          o -> {
            if (!config.location().getRockIds().contains(o.getId())) {
              return false;
            }

            for (RockPosition rockPosition : config.location().getRockPositions()) {
              if (o.getWorldLocation().equals(rockPosition.getRock())) {
                return true;
              }
            }

            return false;
          }) == null;
    }
  }
}
