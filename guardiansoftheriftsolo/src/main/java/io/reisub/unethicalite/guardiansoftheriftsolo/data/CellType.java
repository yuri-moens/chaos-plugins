package io.reisub.unethicalite.guardiansoftheriftsolo.data;

import io.reisub.unethicalite.utils.Constants;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.items.Inventory;

public enum CellType {
  NONE,
  WEAK,
  MEDIUM,
  STRONG,
  OVERCHARGED;

  public static CellType getTypeInInventory() {
    final Item cell = Inventory.getFirst(Predicates.ids(Constants.CELL_IDS));

    if (cell == null) {
      return NONE;
    }

    switch (cell.getId()) {
      case ItemID.WEAK_CELL:
        return WEAK;
      case ItemID.MEDIUM_CELL:
        return MEDIUM;
      case ItemID.STRONG_CELL:
        return STRONG;
      case ItemID.OVERCHARGED_CELL:
        return OVERCHARGED;
      default:
        return NONE;
    }
  }
}