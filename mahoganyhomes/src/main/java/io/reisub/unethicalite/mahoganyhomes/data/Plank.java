package io.reisub.unethicalite.mahoganyhomes.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.ItemID;

@AllArgsConstructor
@Getter
public enum Plank {
  NORMAL(ItemID.PLANK, 1),
  OAK(ItemID.OAK_PLANK, 2),
  TEAK(ItemID.TEAK_PLANK, 3),
  MAHOGANY(ItemID.MAHOGANY_PLANK, 4);

  private final int plankId;
  private final int chatOption;
}
