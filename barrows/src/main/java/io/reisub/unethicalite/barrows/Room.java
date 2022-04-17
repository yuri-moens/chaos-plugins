package io.reisub.unethicalite.barrows;

import com.google.common.collect.ImmutableSet;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.movement.Reachable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.runelite.api.NullObjectID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;

@RequiredArgsConstructor
public enum Room {
    SW(new WorldArea(3528, 9671, 13, 13, 0)),
    W(new WorldArea(3528, 9688, 13, 13, 0)),
    NW(new WorldArea(3528, 9705, 13, 13, 0)),
    N(new WorldArea(3545, 9705, 13, 13, 0)),
    NE(new WorldArea(3562, 9705, 13, 13, 0)),
    E(new WorldArea(3562, 9688, 13, 13, 0)),
    SE(new WorldArea(3562, 9671, 13, 13, 0)),
    S(new WorldArea(3545, 9671, 13, 13, 0)),
    C(new WorldArea(3545, 9688, 13, 13, 0));

    static {
        for (Room room : Room.values()) {
            switch (room) {
                case SW:
                    room.setNorth(Room.W);
                    room.setEast(Room.S);
                    room.setSouth(Room.SE);
                    room.setWest(Room.NW);
                    break;
                case W:
                    room.setNorth(Room.NW);
                    room.setEast(Room.C);
                    room.setSouth(Room.SW);
                    room.setWest(null);
                    break;
                case NW:
                    room.setNorth(Room.NE);
                    room.setEast(Room.N);
                    room.setSouth(Room.W);
                    room.setWest(Room.SW);
                    break;
                case N:
                    room.setNorth(null);
                    room.setEast(Room.NE);
                    room.setSouth(Room.C);
                    room.setWest(Room.NW);
                    break;
                case NE:
                    room.setNorth(Room.NW);
                    room.setEast(Room.SE);
                    room.setSouth(Room.E);
                    room.setWest(Room.N);
                    break;
                case E:
                    room.setNorth(Room.NE);
                    room.setEast(null);
                    room.setSouth(Room.SE);
                    room.setWest(Room.C);
                    break;
                case SE:
                    room.setNorth(Room.E);
                    room.setEast(Room.NE);
                    room.setSouth(Room.SW);
                    room.setWest(Room.S);
                    break;
                case S:
                    room.setNorth(Room.C);
                    room.setEast(Room.SE);
                    room.setSouth(null);
                    room.setWest(Room.SW);
                    break;
                case C:
                    room.setNorth(Room.N);
                    room.setEast(Room.E);
                    room.setSouth(Room.S);
                    room.setWest(Room.W);
                    break;
            }
        }
    }

    private static final Set<Integer> BARROWS_WALLS = ImmutableSet.of(
                    ObjectID.DOOR_20678, NullObjectID.NULL_20681, NullObjectID.NULL_20682, NullObjectID.NULL_20683, NullObjectID.NULL_20684, NullObjectID.NULL_20685, NullObjectID.NULL_20686, NullObjectID.NULL_20687,
                    NullObjectID.NULL_20688, NullObjectID.NULL_20689, NullObjectID.NULL_20690, NullObjectID.NULL_20691, NullObjectID.NULL_20692, NullObjectID.NULL_20693, NullObjectID.NULL_20694, NullObjectID.NULL_20695,
                    NullObjectID.NULL_20696, ObjectID.DOOR_20697, NullObjectID.NULL_20700, NullObjectID.NULL_20701, NullObjectID.NULL_20702, NullObjectID.NULL_20703, NullObjectID.NULL_20704, NullObjectID.NULL_20705,
                    NullObjectID.NULL_20706, NullObjectID.NULL_20707, NullObjectID.NULL_20708, NullObjectID.NULL_20709, NullObjectID.NULL_20710, NullObjectID.NULL_20711, NullObjectID.NULL_20712, NullObjectID.NULL_20713,
                    NullObjectID.NULL_20714, NullObjectID.NULL_20715, NullObjectID.NULL_20728, NullObjectID.NULL_20730
            );

    public static final Predicate<TileObject> DOOR_PREDICATE = object -> BARROWS_WALLS.contains(object.getId())
            && object.hasAction("Open");

    @Getter
    private final WorldArea area;

    @Getter
    @Setter
    private Room north;

    @Getter
    @Setter
    private Room east;

    @Getter
    @Setter
    private Room south;

    @Getter
    @Setter
    private Room west;

    public static Queue<Room> findPath() {
        Queue<Room> path = new LinkedList<>();
        buildPath(path, getCurrentRoom());
        return path;
    }

    public static boolean buildPath(Queue<Room> path, Room current) {
        if (current == null || path.contains(current)) {
            return false;
        } else {
            path.add(current);

            if (current == C) {
                return true;
            }
        }

        switch (current) {
            case NE:
                if (current.canGoSouth()) {
                    if (buildPath(path, current.getSouth())) {
                        return true;
                    }
                }

                if (current.canGoWest()) {
                    if (buildPath(path, current.getWest())) {
                        return true;
                    }
                }

                if (current.canGoEast()) {
                    if (buildPath(path, current.getEast())) {
                        return true;
                    }
                }

                if (current.canGoNorth()) {
                    if (buildPath(path, current.getNorth())) {
                        return true;
                    }
                }
                break;
            case NW:
                if (current.canGoSouth()) {
                    if (buildPath(path, current.getSouth())) {
                        return true;
                    }
                }

                if (current.canGoEast()) {
                    if (buildPath(path, current.getEast())) {
                        return true;
                    }
                }

                if (current.canGoNorth()) {
                    if (buildPath(path, current.getNorth())) {
                        return true;
                    }
                }

                if (current.canGoWest()) {
                    if (buildPath(path, current.getWest())) {
                        return true;
                    }
                }
                break;
            case SE:
                if (current.canGoNorth()) {
                    if (buildPath(path, current.getNorth())) {
                        return true;
                    }
                }

                if (current.canGoWest()) {
                    if (buildPath(path, current.getWest())) {
                        return true;
                    }
                }

                if (current.canGoSouth()) {
                    if (buildPath(path, current.getSouth())) {
                        return true;
                    }
                }

                if (current.canGoEast()) {
                    if (buildPath(path, current.getEast())) {
                        return true;
                    }
                }
                break;
            case SW:
            default:
                if (current.canGoNorth()) {
                    if (buildPath(path, current.getNorth())) {
                        return true;
                    }
                }

                if (current.canGoEast()) {
                    if (buildPath(path, current.getEast())) {
                        return true;
                    }
                }

                if (current.canGoSouth()) {
                    if (buildPath(path, current.getSouth())) {
                        return true;
                    }
                }

                if (current.canGoWest()) {
                    if (buildPath(path, current.getWest())) {
                        return true;
                    }
                }
        }

        path.remove(current);

        return false;
    }

    public static Room getCurrentRoom() {
        for (Room room : Room.values()) {
            if (room.area.contains(Players.getLocal())) {
                return room;
            }
        }

        return null;
    }

    public static boolean isInCorridor() {
        Room current = getCurrentRoom();
        if (current == null) {
            return true;
        }

        WorldPoint center = current.area.getCenter();
        while (Reachable.getCollisionFlag(center) > 0) {
            center = center.dy(-1);
        }

        return !Reachable.isWalkable(center);
    }

    private TileObject getNearestDoor(WorldPoint location1, WorldPoint location2) {
        WorldPoint nearest = Players.getLocal().getWorldLocation().distanceToHypotenuse(location1) < Players.getLocal().getWorldLocation().distanceToHypotenuse(location2) ? location1 : location2;

        return TileObjects.getFirstAt(nearest, DOOR_PREDICATE);
    }

    public TileObject getNorthDoor() {
        WorldPoint location1 = new WorldPoint(area.getX() + 6, area.getY() + 13, area.getPlane());
        WorldPoint location2 = new WorldPoint(area.getX() + 7, area.getY() + 13, area.getPlane());

        return getNearestDoor(location1, location2);
    }

    public TileObject getEastDoor() {
        WorldPoint location1 = new WorldPoint(area.getX() + 13, area.getY() + 6, area.getPlane());
        WorldPoint location2 = new WorldPoint(area.getX() + 13, area.getY() + 7, area.getPlane());

        return getNearestDoor(location1, location2);
    }

    public TileObject getSouthDoor() {
        WorldPoint location1 = new WorldPoint(area.getX() + 6, area.getY(), area.getPlane());
        WorldPoint location2 = new WorldPoint(area.getX() + 7, area.getY(), area.getPlane());

        return getNearestDoor(location1, location2);
    }

    public TileObject getWestDoor() {
        WorldPoint location1 = new WorldPoint(area.getX(), area.getY() + 6, area.getPlane());
        WorldPoint location2 = new WorldPoint(area.getX(), area.getY() + 7, area.getPlane());

        return getNearestDoor(location1, location2);
    }

    private boolean canGoNorth() {
        return getNorthDoor() != null;
    }

    private boolean canGoEast() {
        return getEastDoor() != null;
    }

    private boolean canGoSouth() {
        return getSouthDoor() != null;
    }

    private boolean canGoWest() {
        return getWestDoor() != null;
    }
}
