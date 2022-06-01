package io.reisub.unethicalite.wintertodt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.entities.Players;

@RequiredArgsConstructor
@Getter
public enum Side {
  EAST(new WorldPoint(1638, 3996, 0), new WorldPoint(1638, 3988, 0)),
  WEST(new WorldPoint(1622, 3996, 0), new WorldPoint(1622, 3988, 0));

  private final WorldPoint positionNearBrazier;
  private final WorldPoint positionNearRoots;

  public static Side getNearest() {
    final WorldPoint playerPoint = Players.getLocal().getWorldLocation();

    if (playerPoint.distanceTo(Side.EAST.getPositionNearBrazier())
        > playerPoint.distanceTo(Side.WEST.getPositionNearBrazier())) {
      return Side.WEST;
    } else {
      return Side.EAST;
    }
  }

  public static Side getFurthest() {
    return getNearest() == Side.EAST ? Side.WEST : Side.EAST;
  }
}
