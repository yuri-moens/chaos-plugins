package io.reisub.unethicalite.barrows;

import io.reisub.unethicalite.utils.Utils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.runelite.api.Varbits;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.coords.RectangularArea;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.Vars;

@RequiredArgsConstructor
@Getter
public enum Brother {
  AHRIM(
      "Ahrim",
      new WorldPoint(3566, 3289, 0),
      Varbits.BARROWS_KILLED_AHRIM,
      new WorldPoint(3557, 9703, 3),
      new RectangularArea(new WorldPoint(3550, 9694, 3), 11, 10)),
  DHAROK(
      "Dharok",
      new WorldPoint(3575, 3298, 0),
      Varbits.BARROWS_KILLED_DHAROK,
      new WorldPoint(3556, 9718, 3),
      new RectangularArea(new WorldPoint(3549, 9710, 3), 11, 9)),
  GUTHAN(
      "Guthan",
      new WorldPoint(3577, 3283, 0),
      Varbits.BARROWS_KILLED_GUTHAN,
      new WorldPoint(3534, 9704, 3),
      new RectangularArea(new WorldPoint(3533, 9699, 3), 12, 9)),
  KARIL(
      "Karil",
      new WorldPoint(3566, 3275, 0),
      Varbits.BARROWS_KILLED_KARIL,
      new WorldPoint(3546, 9684, 3),
      new RectangularArea(new WorldPoint(3545, 9678, 3), 12, 10)),
  TORAG(
      "Torag",
      new WorldPoint(3553, 3283, 0),
      Varbits.BARROWS_KILLED_TORAG,
      new WorldPoint(3568, 9683, 3),
      new RectangularArea(new WorldPoint(3564, 9682, 3), 11, 10)),
  VERAC(
      "Verac",
      new WorldPoint(3557, 3298, 0),
      Varbits.BARROWS_KILLED_VERAC,
      new WorldPoint(3578, 9706, 3),
      new RectangularArea(new WorldPoint(3568, 9702, 3), 11, 8));

  private final String name;
  private final WorldPoint location;
  private final int killedVarbit;
  private final WorldPoint pointNextToStairs;
  private final RectangularArea cryptArea;

  @Getter @Setter private boolean inTunnel;

  public static Brother getBrotherByCrypt() {
    if (!Utils.isInRegion(Barrows.CRYPT_REGION)
        || Players.getLocal().getWorldLocation().getPlane() != 3) {
      return null;
    }

    for (Brother brother : Brother.values()) {
      if (brother.cryptArea.contains(Players.getLocal())) {
        return brother;
      }
    }

    return null;
  }

  public boolean isDead() {
    return Vars.getBit(killedVarbit) > 0;
  }

  public boolean isMelee() {
    return this == DHAROK || this == VERAC || this == TORAG || this == GUTHAN;
  }
}
