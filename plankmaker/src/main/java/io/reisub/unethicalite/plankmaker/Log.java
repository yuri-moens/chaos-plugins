package io.reisub.unethicalite.plankmaker;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ItemID;

@RequiredArgsConstructor
@Getter
public enum Log {
  NORMAL(ItemID.LOGS, ItemID.PLANK),
  OAK(ItemID.OAK_LOGS, ItemID.OAK_PLANK),
  TEAK(ItemID.TEAK_LOGS, ItemID.TEAK_PLANK),
  MAHOGANY(ItemID.MAHOGANY_LOGS, ItemID.MAHOGANY_PLANK);

  private final int logId;
  private final int plankId;
}
