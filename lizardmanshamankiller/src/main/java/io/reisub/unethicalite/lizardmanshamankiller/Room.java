package io.reisub.unethicalite.lizardmanshamankiller;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import lombok.Getter;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.coords.RectangularArea;
import net.unethicalite.api.entities.Players;

@Getter
public enum Room {
  
  WEST(new RectangularArea(1289, 10093, 1296, 10100)),
  EAST(new RectangularArea(1326, 10093, 1333, 10100));

  private final RectangularArea area;
  private final List<WorldPoint> optimalTiles;

  Room(RectangularArea area) {
    this.area = area;

    optimalTiles = new ArrayList<>(16);

    for (int i = area.getMinX(); i < area.getMaxX() + 1; i++) {
      optimalTiles.add(new WorldPoint(i, area.getMinY(), 0));
    }

    for (int i = area.getMinY(); i < area.getMaxY() + 1; i++) {
      optimalTiles.add(new WorldPoint(i, area.getMinX(), 0));
    }
  }

  @Nullable
  public static Room getCurrentRoom() {
    for (Room room : Room.values()) {
      if (room.getArea().contains(Players.getLocal())) {
        return room;
      }
    }

    return null;
  }

  @Nullable
  public static Room getEmptyRoom() {
    for (Room room : Room.values()) {
      final List<Player> players = Players.getAll();
      boolean foundPlayer = false;

      for (Player player : players) {
        if (room.getArea().contains(player)) {
          foundPlayer = true;
          break;
        }
      }

      if (foundPlayer) {
        continue;
      }

      return room;
    }

    return null;
  }
}
