package io.reisub.unethicalite.guardiansoftheriftsolo.data;

import com.google.common.collect.ImmutableSet;
import io.reisub.unethicalite.utils.Constants;
import java.util.Comparator;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.NPC;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.TileObjects;

@RequiredArgsConstructor
@Getter
public class CellTile {

  public static final CellTile NORTH_WEST = new CellTile(
      new WorldPoint(3611, 9509, 0), new WorldPoint(3610, 9510, 0), 11418
  );
  public static final CellTile NORTH = new CellTile(
      new WorldPoint(3615, 9510, 0), new WorldPoint(3614, 9511, 11419), 0
  );
  public static final CellTile NORTH_EAST = new CellTile(
      new WorldPoint(3619, 9509, 0), new WorldPoint(3619, 9510, 0), 11418
  );

  public static final Set<CellTile> ALL = ImmutableSet.of(
      NORTH_WEST,
      NORTH,
      NORTH_EAST
  );

  private final WorldPoint location;
  private final WorldPoint npcLocation;
  private final int npcId;

  public static CellTile getLowestType() {
    return ALL.stream().min(Comparator.comparing(CellTile::getType)).orElseThrow();
  }

  public static CellTile getLowestHealth() {
    return ALL.stream().min(Comparator.comparing(CellTile::getHealth)).orElseThrow();
  }

  public TileObject getObject() {
    return TileObjects.getFirstAt(location, Predicates.ids(Constants.ACTIVE_CELL_TILE_IDS));
  }

  public CellType getType() {
    final TileObject cellTile = getObject();

    if (cellTile == null) {
      return CellType.NONE;
    }

    switch (cellTile.getId()) {
      case ObjectID.WEAK_CELL_TILE:
        return CellType.WEAK;
      case ObjectID.MEDIUM_CELL_TILE:
        return CellType.MEDIUM;
      case ObjectID.STRONG_CELL_TILE:
        return CellType.STRONG;
      case ObjectID.OVERPOWERED_CELL_TILE:
        return CellType.OVERCHARGED;
      default:
        return CellType.NONE;
    }
  }

  public int getHealth() {
    final NPC barrier = NPCs.getNearest(npcLocation, npcId);

    if (barrier == null) {
      return 0;
    }

    return barrier.getHealthRatio();
  }
}
