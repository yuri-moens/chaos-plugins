package io.reisub.unethicalite.agility;

import lombok.Getter;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;

public class ObstacleArea extends WorldArea {
  @Getter private final int id;

  public ObstacleArea(WorldPoint sw, WorldPoint ne, int id) {
    super(sw, ne);

    this.id = id;
  }
}
