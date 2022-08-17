package io.reisub.unethicalite.guardiansoftheriftsolo.data;

import io.reisub.unethicalite.utils.Utils;
import java.util.function.BooleanSupplier;
import lombok.RequiredArgsConstructor;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;

@RequiredArgsConstructor
public enum AreaType {
  MAIN(() -> {
    if (!Utils.isInMapRegion(14484)) {
      return false;
    }

    return Players.getLocal().getWorldX() < 3634 && Players.getLocal().getWorldX() > 3595;
  }),
  LARGE_REMAINS(() -> {
    if (!Utils.isInMapRegion(14484)) {
      return false;
    }

    return Players.getLocal().getWorldX() > 3634;
  }),
  HUGE_REMAINS(() -> {
    if (!Utils.isInMapRegion(14484)) {
      return false;
    }

    return Players.getLocal().getWorldX() < 3595;
  }),
  ALTAR(() -> TileObjects.getNearest("Altar") != null),
  UNKNOWN(() -> true);

  private final BooleanSupplier test;

  public static AreaType getCurrent() {
    for (AreaType type : AreaType.values()) {
      if (type.test.getAsBoolean()) {
        return type;
      }
    }

    return UNKNOWN;
  }
}
