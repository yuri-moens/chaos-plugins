package io.reisub.unethicalite.barrows;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Potential {
  WHATEVER(0, 0, false),
  DEATH_RUNES(631, 755, false),
  MAX_DEATH_RUNES(631, 755, true),
  BLOOD_RUNES(756, 880, false),
  MAX_BLOOD_RUNES(756, 880, true),
  MAX(881, 1012, true);

  private final int minimum;
  private final int maximum;
  private final boolean tryMaximum;
}
