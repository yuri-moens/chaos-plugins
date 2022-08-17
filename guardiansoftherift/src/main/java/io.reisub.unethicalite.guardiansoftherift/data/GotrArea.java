package io.reisub.unethicalite.guardiansoftherift.data;

import io.reisub.unethicalite.utils.Utils;
import java.util.function.BooleanSupplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;

@RequiredArgsConstructor
@Getter
public enum GotrArea {
  ALTAR(() -> TileObjects.getNearest("Altar") != null),
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
  UNKNOWN(() -> true);

  private final BooleanSupplier test;

  public static GotrArea getCurrent() {
    for (GotrArea type : GotrArea.values()) {
      if (type.test.getAsBoolean()) {
        return type;
      }
    }

    return UNKNOWN;
  }
}
