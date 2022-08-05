package io.reisub.unethicalite.fletching.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ItemID;

@RequiredArgsConstructor
@Getter
public enum Metal {
  BRONZE(ItemID.BRONZE_ARROWTIPS, ItemID.BRONZE_BOLTS_UNF, ItemID.BRONZE_DART_TIP),
  IRON(ItemID.IRON_ARROWTIPS, ItemID.IRON_BOLTS_UNF, ItemID.IRON_DART_TIP),
  STEEL(ItemID.STEEL_ARROWTIPS, ItemID.STEEL_BOLTS_UNF, ItemID.STEEL_DART_TIP),
  MITHRIL(ItemID.MITHRIL_ARROWTIPS, ItemID.MITHRIL_BOLTS_UNF, ItemID.MITHRIL_DART_TIP),
  BROAD(ItemID.BROAD_ARROWHEADS, ItemID.UNFINISHED_BROAD_BOLTS, -1),
  ADAMANT(ItemID.ADAMANT_ARROWTIPS, ItemID.ADAMANT_BOLTSUNF, ItemID.ADAMANT_DART_TIP),
  RUNE(ItemID.RUNE_ARROWTIPS, ItemID.RUNITE_BOLTS_UNF, ItemID.RUNE_DART_TIP),
  AMETHYST(ItemID.AMETHYST_ARROWTIPS, ItemID.AMETHYST_BOLT_TIPS, ItemID.AMETHYST_DART_TIP),
  DRAGON(ItemID.DRAGON_ARROWTIPS, ItemID.DRAGON_BOLTS_UNF, ItemID.DRAGON_DART_TIP),
  WOLFBONE(ItemID.WOLFBONE_ARROWTIPS, -1, -1),
  ;

  private final int arrowheadId;
  private final int unfinishedBoltsId;
  private final int unfinishedDartsId;
}
