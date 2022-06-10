package io.reisub.unethicalite.giantsfoundry.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.ItemID;

@AllArgsConstructor
@Getter
public enum Alloy {
  BRONZE(ItemID.BRONZE_BAR, 1),
  IRON(ItemID.IRON_BAR, 2),
  STEEL(ItemID.STEEL_BAR, 3),
  MITHRIL(ItemID.MITHRIL_BAR, 4),
  ADAMANTITE(ItemID.ADAMANTITE_BAR, 5),
  RUNITE(ItemID.RUNITE_BAR, 6);

  private final int barId;
  private final int dialogIndex;
}
