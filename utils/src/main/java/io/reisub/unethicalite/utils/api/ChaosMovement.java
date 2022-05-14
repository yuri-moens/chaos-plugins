package io.reisub.unethicalite.utils.api;

import com.google.common.collect.Sets;
import dev.unethicalite.api.commons.Rand;
import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.movement.Movement;
import dev.unethicalite.api.movement.Reachable;
import dev.unethicalite.api.packets.MovementPackets;
import dev.unethicalite.client.Static;
import java.util.Set;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Locatable;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;

public class ChaosMovement {

  private static final int DEFAULT_TIMEOUT = 100;
  private static final int DESTINATION_DISTANCE = 8;

  public static boolean interrupted;

  public static void walkTo(WorldPoint destination) {
    walkTo(destination, 0);
  }

  public static void walkTo(WorldPoint destination, Runnable task) {
    walkTo(destination, 0, task);
  }

  public static void walkTo(WorldPoint destination, int radius) {
    walkTo(destination, radius, null, DEFAULT_TIMEOUT);
  }

  public static void walkTo(WorldPoint destination, int radius, Runnable task) {
    walkTo(destination, radius, task, DEFAULT_TIMEOUT);
  }

  public static void walkTo(WorldPoint destination, int radius, Runnable task, int tickTimeout) {
    walkTo(destination, radius, task, tickTimeout, DESTINATION_DISTANCE);
  }

  public static void walkTo(
      WorldPoint destination, int radius, Runnable task, int tickTimeout, int destinationDistance) {
    int start = Static.getClient().getTickCount();

    if (radius > 0) {
      destination =
          destination.dx(Rand.nextInt(-radius, radius + 1)).dy(Rand.nextInt(-radius, radius + 1));
    }

    do {
      if (!Movement.isWalking() && Static.getClient().getGameState() != GameState.LOADING) {
        Movement.walkTo(destination);

        if (!Players.getLocal().isMoving()) {
          Time.sleepTick();
        }
      } else if (task != null) {
        Static.getClientThread().invoke(task);
      }

      Time.sleepTick();
    } while (!interrupted
        && Players.getLocal().distanceTo(destination) > destinationDistance
        && Static.getClient().getTickCount() <= start + tickTimeout
        && (Static.getClient().getGameState() == GameState.LOADING
        || Static.getClient().getGameState() == GameState.LOGGED_IN));

    interrupted = false;
  }

  public static void sendMovementPacket(WorldPoint worldPoint) {
    sendMovementPacket(worldPoint, false);
  }

  public static void sendMovementPacket(WorldPoint worldPoint, boolean ctrlDown) {
    sendMovementPacket(worldPoint.getX(), worldPoint.getY(), ctrlDown);
  }

  public static void sendMovementPacket(int x, int y) {
    sendMovementPacket(x, y, false);
  }

  public static void sendMovementPacket(int x, int y, boolean ctrlDown) {
    Client client = Static.getClient();
    client.setDestinationX(x - client.getBaseX());
    client.setDestinationY(y - client.getBaseY());

    Static.getClientThread().invoke(() -> MovementPackets.sendMovement(x, y, ctrlDown));
  }

  public static boolean openDoor(Locatable target) {
    return openDoor(target, Sets.newHashSet());
  }

  public static boolean openDoor(Locatable target, final Set<WorldPoint> ignoreLocations) {
    if (target == null) {
      return false;
    }

    final WorldPoint targetLocation = target.getWorldLocation();

    final TileObject door = TileObjects.getNearest(
        targetLocation,
        o -> o.getName().equals("Door")
            && o.hasAction("Open")
            && !ignoreLocations.contains(o.getWorldLocation())
    );

    if (door == null) {
      return false;
    }

    if (!Reachable.isInteractable(door)) {
      ignoreLocations.add(door.getWorldLocation());

      if (!openDoor(door, ignoreLocations)) {
        return false;
      }
    }

    GameThread.invoke(() -> door.interact("Open"));

    final WorldPoint tile = door.getWorldLocation();

    return Time.sleepTicksUntil(
        () -> TileObjects.getFirstAt(tile, o -> o.hasAction("Open")) == null, 15
    );
  }
}
