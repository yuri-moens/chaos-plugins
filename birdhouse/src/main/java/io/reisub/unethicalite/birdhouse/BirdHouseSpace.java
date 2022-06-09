package io.reisub.unethicalite.birdhouse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.TileObject;
import net.runelite.api.VarPlayer;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.Vars;

@RequiredArgsConstructor
@Getter
public enum BirdHouseSpace {
  MEADOW_SOUTH("Mushroom Meadow (South)",
      VarPlayer.BIRD_HOUSE_MEADOW_SOUTH, 30566, new WorldPoint(3679, 3814, 0)),
  MEADOW_NORTH("Mushroom Meadow (North)",
      VarPlayer.BIRD_HOUSE_MEADOW_NORTH, 30565, new WorldPoint(3677, 3881, 0)),
  VALLEY_SOUTH("Verdant Valley (Southwest)",
      VarPlayer.BIRD_HOUSE_VALLEY_SOUTH, 30568, new WorldPoint(3763, 3754, 0)),
  VALLEY_NORTH("Verdant Valley (Northeast)",
      VarPlayer.BIRD_HOUSE_VALLEY_NORTH, 30567, new WorldPoint(3768, 3760, 0)),
  ;

  private final String name;
  private final VarPlayer varp;
  private final int objectId;
  private final WorldPoint location;

  public static TileObject getNearest(final BirdHouseState state) {
    TileObject birdHouse = null;

    for (BirdHouseSpace space : BirdHouseSpace.values()) {
      if (BirdHouseState.fromVarpValue(Vars.getVarp(space.getVarp().getId())) == state) {
        final TileObject tempBirdHouse = TileObjects.getNearest(space.getObjectId());

        if (tempBirdHouse == null) {
          continue;
        }

        if (birdHouse == null
            || Players.getLocal().distanceTo(tempBirdHouse)
            < Players.getLocal().distanceTo(birdHouse)) {
          birdHouse = tempBirdHouse;
        }
      }
    }

    return birdHouse;
  }
}
