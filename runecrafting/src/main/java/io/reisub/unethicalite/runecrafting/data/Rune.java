package io.reisub.unethicalite.runecrafting.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.coords.WorldPoint;

@RequiredArgsConstructor
@Getter
public enum Rune {
  TRUE_BLOOD(),
  ;

  private final WorldPoint bankPoint;
  private final WorldPoint altarPoint;

  Rune() {
    this(null, null);
  }

  Rune(WorldPoint altarPoint) {
    this(null, altarPoint);
  }
}
