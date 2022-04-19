package io.reisub.unethicalite.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.ItemID;

@AllArgsConstructor
@Getter
public enum Log {
  NORMAL(ItemID.LOGS),
  OAK(ItemID.OAK_LOGS),
  WILLOW(ItemID.WILLOW_LOGS),
  TEAK(ItemID.TEAK_LOGS),
  MAPLE(ItemID.MAPLE_LOGS),
  MAHOGANY(ItemID.MAHOGANY_LOGS),
  YEW(ItemID.YEW_LOGS),
  MAGIC(ItemID.MAGIC_LOGS),
  REDWOOD(ItemID.REDWOOD_LOGS);

  private final int id;
}
