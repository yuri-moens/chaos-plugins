package io.reisub.unethicalite.daeyaltessence.data;

import com.google.common.collect.Lists;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.input.naturalmouse.util.Pair;

@RequiredArgsConstructor
@Getter
public enum Rock {

  NORTH(new WorldPoint(3675, 9766, 2), Lists.newArrayList(
      new Pair<>(new WorldPoint(3674, 9764, 2), new WorldPoint(3675, 9764, 2))
  )),
  EAST(new WorldPoint(3688, 9756, 2), Lists.newArrayList(
      new Pair<>(new WorldPoint(3686, 9757, 2), new WorldPoint(3686, 9756, 2))
  )),
  SOUTH(new WorldPoint(3672, 9751, 2), Lists.newArrayList(
      new Pair<>(new WorldPoint(3671, 9753, 2), new WorldPoint(3672, 9753, 2)),
      new Pair<>(new WorldPoint(3674, 9750, 2), new WorldPoint(3674, 9751, 2))
  ));


  private final WorldPoint rockPoint;
  private final List<Pair<WorldPoint, WorldPoint>> adjacentPointPairs;

  public static Rock getActiveRock() {
    final TileObject object = TileObjects.getNearest(ObjectID.DAEYALT_ESSENCE_39095);
    if (object == null) {
      return null;
    }

    for (Rock rock : Rock.values()) {
      if (rock.rockPoint.equals(object.getWorldLocation())) {
        return rock;
      }
    }

    return null;
  }

  public Pair<WorldPoint, WorldPoint> getNearestPoints() {
    Pair<WorldPoint, WorldPoint> nearest = null;

    for (Pair<WorldPoint, WorldPoint> pair : adjacentPointPairs) {
      if (nearest == null) {
        nearest = pair;
        continue;
      }

      if (Players.getLocal().distanceTo(nearest.x) > Players.getLocal().distanceTo(pair.x)) {
        nearest = pair;
      }
    }

    return nearest;
  }

  public TileObject getObject() {
    return TileObjects.getNearest(ObjectID.DAEYALT_ESSENCE_39095);
  }
}
