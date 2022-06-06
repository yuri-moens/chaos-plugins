package io.reisub.unethicalite.sulliuscep;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.NullObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.movement.Reachable;

@RequiredArgsConstructor
@Getter
public enum SulliuscepObject {

  SULLIUSCEP_1(
      NullObjectID.NULL_31420,
      new WorldPoint(3683, 3758, 0),
      0,
      null
  ),
  SULLIUSCEP_2(
      NullObjectID.NULL_31421,
      new WorldPoint(3678, 3733, 0),
      1,
      ImmutableList.of(new WorldPoint(3678, 3743, 0))
  ),
  SULLIUSCEP_3(
      NullObjectID.NULL_31422,
      new WorldPoint(3683, 3775, 0),
      2,
      ImmutableList.of(
          new WorldPoint(3669, 3746, 0),
          new WorldPoint(3671, 3760, 0),
          new WorldPoint(3672, 3764, 0)
      )
  ),
  SULLIUSCEP_4(
      NullObjectID.NULL_31423,
      new WorldPoint(3663, 3781, 0),
      3,
      ImmutableList.of(new WorldPoint(3674, 3771, 0))
  ),
  SULLIUSCEP_5(
      NullObjectID.NULL_31424,
      new WorldPoint(3663, 3802, 0),
      4,
      ImmutableList.of(
          new WorldPoint(3666, 3788, 0),
          new WorldPoint(3670, 3792, 0),
          new WorldPoint(3672, 3801, 0)
      )
  ),
  SULLIUSCEP_6(
      NullObjectID.NULL_31425,
      new WorldPoint(3678, 3806, 0),
      5,
      null
  );

  private final int id;
  private final WorldPoint location;
  private final int varb;
  private final ImmutableList<WorldPoint> obstacles;

  public TileObject getObject() {
    return TileObjects.getNearest(id);
  }

  public boolean isReachable() {
    final TileObject o = getObject();

    if (o == null) {
      return false;
    }

    return Reachable.isInteractable(o);
  }
}
