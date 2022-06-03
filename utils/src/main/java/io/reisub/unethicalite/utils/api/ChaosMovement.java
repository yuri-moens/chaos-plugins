package io.reisub.unethicalite.utils.api;

import com.google.common.collect.Sets;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.enums.PortalTeleport;
import java.util.Set;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ItemID;
import net.runelite.api.Locatable;
import net.runelite.api.MenuAction;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Rand;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.magic.SpellBook.Standard;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.movement.Reachable;
import net.unethicalite.api.packets.MovementPackets;
import net.unethicalite.api.widgets.Widgets;
import net.unethicalite.client.Static;

public class ChaosMovement {

  private static final int DEFAULT_TIMEOUT = 100;
  private static final int DESTINATION_DISTANCE = 8;

  public static boolean interrupted;

  public static void walk(WorldPoint destination, final int rand) {
    walk(destination, rand, rand);
  }

  public static void walk(WorldPoint destination, final int x, final int y) {
    destination = destination
        .dx(Rand.nextInt(-x, x + 1))
        .dy(Rand.nextInt(-y, y + 1));

    Movement.walk(destination);
  }

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

  public static boolean teleportToHouse() {
    if (Standard.TELEPORT_TO_HOUSE.canCast()) {
      Standard.TELEPORT_TO_HOUSE.cast();
    } else if (Inventory.contains(Predicates.ids(Constants.CONSTRUCTION_CAPE_IDS))
        || Equipment.contains(Predicates.ids(Constants.CONSTRUCTION_CAPE_IDS))) {
      Interact.interactWithInventoryOrEquipment(
          Constants.CONSTRUCTION_CAPE_IDS,
          "Teleport",
          null,
          -1
      );
    } else if (Inventory.contains(ItemID.TELEPORT_TO_HOUSE)) {
      Inventory.getFirst(ItemID.TELEPORT_TO_HOUSE).interact("Break");
    } else {
      return false;
    }

    return Time.sleepTicksUntil(() -> TileObjects.getNearest(ObjectID.PORTAL_4525) != null, 10);
  }

  public static boolean teleportThroughHouse(PortalTeleport portalTeleport) {
    return teleportThroughHouse(portalTeleport, false);
  }

  public static boolean teleportThroughHouse(PortalTeleport portalTeleport, int energyThreshold) {
    return teleportThroughHouse(portalTeleport, false, energyThreshold);
  }

  public static boolean teleportThroughHouse(PortalTeleport portalTeleport, boolean forceNexus) {
    return teleportThroughHouse(portalTeleport, forceNexus, 30);
  }

  public static boolean teleportThroughHouse(PortalTeleport portalTeleport,
      boolean forceNexus, int energyThreshold) {
    if (!Static.getClient().isInInstancedRegion()) {
      if (!teleportToHouse()) {
        return false;
      }
    }

    if (Movement.getRunEnergy() < energyThreshold) {
      final TileObject pool =
          TileObjects.getNearest(Predicates.ids(Constants.REJUVENATION_POOL_IDS));

      if (pool != null) {
        GameThread.invoke(() -> pool.interact(0));

        Time.sleepTicksUntil(() -> Movement.getRunEnergy() >= 99, 10);
        Time.sleepTick();

        if (!Movement.isRunEnabled()) {
          Movement.toggleRun();
        }
      }
    }

    if (forceNexus) {
      if (!Time.sleepTicksUntil(() ->
          TileObjects.getNearest(Predicates.ids(Constants.PORTAL_NEXUS_IDS)) != null, 5)) {
        return false;
      }

      Time.sleepTick();
      teleportThroughPortalNexus(portalTeleport);
    }

    if (Time.sleepTicksUntil(() ->
        TileObjects.getNearest(portalTeleport.getPortalId()) != null, 5)) {
      Time.sleepTick();
      return teleportThroughPortal(portalTeleport);
    } else {
      Time.sleepTick();
      return teleportThroughPortalNexus(portalTeleport);
    }
  }

  public static boolean teleportThroughPortal(PortalTeleport portalTeleport) {
    final TileObject portal = TileObjects.getNearest(portalTeleport.getPortalId());

    GameThread.invoke(() -> portal.interact("Enter"));

    return Time.sleepTicksUntil(() -> !Static.getClient().isInInstancedRegion(), 30);
  }

  public static boolean teleportThroughPortalNexus(PortalTeleport portalTeleport) {
    final TileObject portalNexus
        = TileObjects.getNearest(Predicates.ids(Constants.PORTAL_NEXUS_IDS));

    if (portalNexus == null) {
      return false;
    }

    final String destination = portalTeleport.getName().toLowerCase();

    for (String action : portalNexus.getActions()) {
      if (action != null && action.toLowerCase().contains(destination)) {
        GameThread.invoke(() -> portalNexus.interact(action));

        return Time.sleepTicksUntil(() -> !Static.getClient().isInInstancedRegion(), 30);
      }
    }

    GameThread.invoke(() -> portalNexus.interact("Teleport Menu"));

    if (!Time.sleepTicksUntil(() -> Widgets.isVisible(Widgets.get(17, 2)), 30)) {
      return false;
    }

    final Widget[] children = Widgets.get(17, 12).getChildren();

    if (children == null) {
      return false;
    }

    int i;

    for (i = 0; i < children.length; i++) {
      if (children[i].getText().toLowerCase().contains(destination)) {
        break;
      }
    }

    final Widget destinationWidget = Widgets.get(17, 13, i);

    if (destinationWidget == null) {
      return false;
    }

    destinationWidget.interact(
        0,
        MenuAction.WIDGET_CONTINUE.getId(),
        destinationWidget.getIndex(),
        destinationWidget.getId()
    );

    return Time.sleepTicksUntil(() -> !Static.getClient().isInInstancedRegion(), 10);
  }
}
