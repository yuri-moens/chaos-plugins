package io.reisub.unethicalite.utils.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OverheadPrayer {
  PROTECT_FROM_MAGIC(ChaosPrayer.PROTECT_FROM_MAGIC),
  PROTECT_FROM_MISSILES(ChaosPrayer.PROTECT_FROM_MISSILES),
  PROTECT_FROM_MELEE(ChaosPrayer.PROTECT_FROM_MELEE),
  RETRIBUTION(ChaosPrayer.RETRIBUTION),
  REDEMPTION(ChaosPrayer.REDEMPTION),
  SMITE(ChaosPrayer.SMITE);

  private final ChaosPrayer prayer;
}
