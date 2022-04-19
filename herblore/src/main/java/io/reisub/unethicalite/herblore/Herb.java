package io.reisub.unethicalite.herblore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.ItemID;

@AllArgsConstructor
@Getter
public enum Herb {
  ALL(-1, -1, -1),
  GUAM_LEAF(ItemID.GRIMY_GUAM_LEAF, ItemID.GUAM_LEAF, ItemID.GUAM_POTION_UNF),
  MARRENTILL(ItemID.GRIMY_MARRENTILL, ItemID.MARRENTILL, ItemID.MARRENTILL_POTION_UNF),
  TARROMIN(ItemID.GRIMY_TARROMIN, ItemID.TARROMIN, ItemID.TARROMIN_POTION_UNF),
  HARRALANDER(ItemID.GRIMY_HARRALANDER, ItemID.HARRALANDER, ItemID.HARRALANDER_POTION_UNF),
  RANARR_WEED(ItemID.GRIMY_RANARR_WEED, ItemID.RANARR_WEED, ItemID.RANARR_POTION_UNF),
  TOADFLAX(ItemID.GRIMY_TOADFLAX, ItemID.TOADFLAX, ItemID.TOADFLAX_POTION_UNF),
  IRIT_LEAF(ItemID.GRIMY_IRIT_LEAF, ItemID.IRIT_LEAF, ItemID.IRIT_POTION_UNF),
  AVANTOE(ItemID.GRIMY_AVANTOE, ItemID.AVANTOE, ItemID.AVANTOE_POTION_UNF),
  KWUARM(ItemID.GRIMY_KWUARM, ItemID.KWUARM, ItemID.KWUARM_POTION_UNF),
  SNAPDRAGON(ItemID.GRIMY_SNAPDRAGON, ItemID.SNAPDRAGON, ItemID.SNAPDRAGON_POTION_UNF),
  CADANTINE(ItemID.GRIMY_CADANTINE, ItemID.CADANTINE, ItemID.CADANTINE_POTION_UNF),
  LANTADYME(ItemID.GRIMY_LANTADYME, ItemID.LANTADYME, ItemID.LANTADYME_POTION_UNF),
  DWARF_WEED(ItemID.GRIMY_DWARF_WEED, ItemID.DWARF_WEED, ItemID.DWARF_WEED_POTION_UNF),
  TORSTOL(ItemID.GRIMY_TORSTOL, ItemID.TORSTOL, ItemID.TORSTOL_POTION_UNF);

  private final int grimyId;
  private final int cleanId;
  private final int unfinishedId;

  public static int[] getAllGrimyIds() {
    final int[] ids = new int[Herb.values().length - 1];
    int i = 0;

    for (Herb herb : Herb.values()) {
      if (herb == Herb.ALL) {
        continue;
      }

      ids[i++] = herb.getGrimyId();
    }

    return ids;
  }

  public static int[] getAllCleanIds() {
    final int[] ids = new int[Herb.values().length - 1];
    int i = 0;

    for (Herb herb : Herb.values()) {
      if (herb == Herb.ALL) {
        continue;
      }

      ids[i++] = herb.getCleanId();
    }

    return ids;
  }
}
