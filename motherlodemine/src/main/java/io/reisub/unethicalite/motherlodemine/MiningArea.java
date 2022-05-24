package io.reisub.unethicalite.motherlodemine;

import com.google.common.collect.ImmutableSet;
import dev.unethicalite.api.coords.RectangularArea;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.movement.Reachable;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.TileObject;
import net.runelite.api.coords.Direction;
import net.runelite.api.coords.WorldPoint;

@RequiredArgsConstructor
@Getter
public enum MiningArea {
  UPSTAIRS(
      new RectangularArea(
          new WorldPoint(3747, 5676, 0),
          new WorldPoint(3754, 5684, 0)
      ),
      null
  ),
  BEHIND_SHORTCUT(
      new RectangularArea(
          new WorldPoint(3764, 5657, 0),
          new WorldPoint(3773, 5668, 0)
      ),
      ImmutableSet.of(
          new WorldPoint(3773, 5656, 0),
          new WorldPoint(3764, 5665, 0)
      )
  ),
  NORTH(
      new RectangularArea(
          new WorldPoint(3733, 5687, 0),
          new WorldPoint(3743, 5692, 0)
      ),
      null
  );

  private final RectangularArea miningArea;
  private final Set<WorldPoint> ignorePoints;

  public TileObject getNearestVein() {
    final List<TileObject> oreVeins = TileObjects.getAll(
        o -> {
          if (ignorePoints == null) {
            return o.getName().equals("Ore vein")
                && o.hasAction("Mine")
                && miningArea.contains(o.getWorldLocation());
          } else {
            return o.getName().equals("Ore vein")
                && o.hasAction("Mine")
                && miningArea.contains(o.getWorldLocation())
                && !ignorePoints.contains(o.getWorldLocation());
          }
        }
    );

    TileObject oreVein = null;
    float nearest = Float.MAX_VALUE;
    final WorldPoint current = Players.getLocal().getWorldLocation();

    for (TileObject o : oreVeins) {
      for (Direction dir : Direction.values()) {
        final WorldPoint neighbour = Reachable.getNeighbour(dir, o.getWorldLocation());

        if (Reachable.isWalkable(neighbour)) {
          final float distance = current.distanceToHypotenuse(neighbour);

          if (oreVein == null || distance < nearest) {
            nearest = distance;
            oreVein = o;
          } else if (distance == nearest) {
            oreVein =
                current.distanceToHypotenuse(oreVein.getWorldLocation())
                    > current.distanceToHypotenuse(o.getWorldLocation())
                    ? o
                    : oreVein;
          }
        }
      }
    }

    return oreVein;
  }
}
