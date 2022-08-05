package io.reisub.unethicalite.fletching.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ItemID;

@RequiredArgsConstructor
@Getter
public enum Log {
  NORMAL(ItemID.LOGS, ItemID.SHORTBOW_U, ItemID.LONGBOW_U),
  OAK(ItemID.OAK_LOGS, ItemID.OAK_SHORTBOW_U, ItemID.OAK_LONGBOW_U),
  WILLOW(ItemID.WILLOW_LOGS, ItemID.WILLOW_SHORTBOW_U, ItemID.WILLOW_LONGBOW_U),
  MAPLE(ItemID.MAPLE_LOGS, ItemID.MAPLE_SHORTBOW_U, ItemID.MAPLE_LONGBOW_U),
  YEW(ItemID.YEW_LOGS, ItemID.YEW_SHORTBOW_U, ItemID.YEW_LONGBOW_U),
  MAGIC(ItemID.MAGIC_LOGS, ItemID.MAGIC_SHORTBOW_U, ItemID.MAGIC_LONGBOW_U),
  ;

  private final int id;
  private final int shortbowUnfinishedId;
  private final int longbowUnfinishedId;
}
