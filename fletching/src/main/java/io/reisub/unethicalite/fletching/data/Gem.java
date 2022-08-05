package io.reisub.unethicalite.fletching.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ItemID;

@RequiredArgsConstructor
@Getter
public enum Gem {
  OPAL(ItemID.BRONZE_BOLTS, ItemID.OPAL_BOLT_TIPS),
  JADE(ItemID.BLURITE_BOLTS, ItemID.JADE_BOLT_TIPS),
  PEARL(ItemID.IRON_BOLTS, ItemID.PEARL_BOLT_TIPS),
  RED_TOPAZ(ItemID.STEEL_BOLTS, ItemID.TOPAZ_BOLT_TIPS),
  SAPPHIRE(ItemID.MITHRIL_BOLTS, ItemID.SAPPHIRE_BOLT_TIPS),
  EMERALD(ItemID.MITHRIL_BOLTS, ItemID.EMERALD_BOLT_TIPS),
  RUBY(ItemID.ADAMANT_BOLTS, ItemID.RUBY_BOLT_TIPS),
  DIAMOND(ItemID.ADAMANT_BOLTS, ItemID.DIAMOND_BOLT_TIPS),
  DRAGONSTONE(ItemID.RUNITE_BOLTS, ItemID.DRAGONSTONE_BOLT_TIPS),
  ONYX(ItemID.RUNITE_BOLTS, ItemID.ONYX_BOLT_TIPS),
  AMETHYST(ItemID.BROAD_BOLTS, ItemID.AMETHYST_BOLT_TIPS),
  ;

  private final int boltId;
  private final int tipId;
}
