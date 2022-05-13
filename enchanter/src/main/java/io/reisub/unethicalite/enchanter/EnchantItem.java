package io.reisub.unethicalite.enchanter;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.ItemID;

@AllArgsConstructor
@Getter
public enum EnchantItem {
  ALL(0),
  SAPPHIRE_RING(ItemID.SAPPHIRE_RING),
  SAPPHIRE_NECKLACE(ItemID.SAPPHIRE_NECKLACE),
  SAPPHIRE_BRACELET(ItemID.SAPPHIRE_BRACELET_11072),
  SAPPHIRE_AMULET(ItemID.SAPPHIRE_AMULET),
  OPAL_RING(ItemID.OPAL_RING),
  OPAL_BRACELET(ItemID.OPAL_BRACELET),
  OPAL_NECKLACE(ItemID.OPAL_NECKLACE),
  OPAL_AMULET(ItemID.OPAL_AMULET),
  EMERALD_RING(ItemID.EMERALD_RING),
  EMERALD_NECKLACE(ItemID.EMERALD_NECKLACE),
  EMERALD_BRACELET(ItemID.EMERALD_BRACELET),
  EMERALD_AMULET(ItemID.EMERALD_AMULET),
  PRENATURE_AMULET(ItemID.PRENATURE_AMULET),
  JADE_RING(ItemID.JADE_RING),
  JADE_NECKLACE(ItemID.JADE_NECKLACE),
  JADE_BRACELET(ItemID.JADE_BRACELET),
  JADE_AMULET(ItemID.JADE_AMULET),
  RUBY_RING(ItemID.RUBY_RING),
  RUBY_BRACELET(ItemID.RUBY_BRACELET),
  RUBY_AMULET(ItemID.RUBY_AMULET),
  RUBY_NECKLACE(ItemID.RUBY_NECKLACE),
  TOPAZ_RING(ItemID.TOPAZ_RING),
  TOPAZ_BRACELET(ItemID.TOPAZ_BRACELET),
  TOPAZ_NECKLACE(ItemID.TOPAZ_NECKLACE),
  TOPAZ_AMULET(ItemID.TOPAZ_AMULET),
  DIAMOND_RING(ItemID.DIAMOND_RING),
  DIAMOND_NECKLACE(ItemID.DIAMOND_NECKLACE),
  DIAMOND_BRACELET(ItemID.DIAMOND_BRACELET),
  DIAMOND_AMULET(ItemID.DIAMOND_AMULET),
  DRAGONSTONE_RING(ItemID.DRAGONSTONE_RING),
  DRAGONSTONE_NECKLACE(ItemID.DRAGON_NECKLACE),
  DRAGONSTONE_BRACELET(ItemID.DRAGONSTONE_BRACELET),
  DRAGONSTONE_AMULET(ItemID.DRAGONSTONE_AMULET),
  ONYX_RING(ItemID.ONYX_RING),
  ONYX_NECKLACE(ItemID.ONYX_NECKLACE),
  ONYX_BRACELET(ItemID.ONYX_BRACELET),
  ONYX_AMULET(ItemID.ONYX_AMULET),
  ZENYTE_RING(ItemID.ZENYTE_RING),
  ZENYTE_NECKLACE(ItemID.ZENYTE_NECKLACE),
  ZENYTE_BRACELET(ItemID.ZENYTE_BRACELET),
  ZENYTE_AMULET(ItemID.ZENYTE_AMULET);

  private final int id;

  public static Set<Integer> getAllItemsFor(EnchantSpell spell) {
    switch (spell) {
      case LEVEL_1:
        return ImmutableSet.of(
            SAPPHIRE_RING.id,
            SAPPHIRE_NECKLACE.id,
            SAPPHIRE_BRACELET.id,
            SAPPHIRE_AMULET.id,
            OPAL_RING.id,
            OPAL_BRACELET.id,
            OPAL_NECKLACE.id,
            OPAL_AMULET.id);
      case LEVEL_2:
        return ImmutableSet.of(
            EMERALD_RING.id,
            EMERALD_NECKLACE.id,
            EMERALD_BRACELET.id,
            EMERALD_AMULET.id,
            PRENATURE_AMULET.id,
            JADE_RING.id,
            JADE_NECKLACE.id,
            JADE_BRACELET.id,
            JADE_AMULET.id);
      case LEVEL_3:
        return ImmutableSet.of(
            RUBY_RING.id,
            RUBY_BRACELET.id,
            RUBY_AMULET.id,
            RUBY_NECKLACE.id,
            TOPAZ_RING.id,
            TOPAZ_BRACELET.id,
            TOPAZ_NECKLACE.id,
            TOPAZ_AMULET.id);
      case LEVEL_4:
        return ImmutableSet.of(
            DIAMOND_RING.id, DIAMOND_NECKLACE.id, DIAMOND_BRACELET.id, DIAMOND_AMULET.id);
      case LEVEL_5:
        return ImmutableSet.of(
            DRAGONSTONE_RING.id,
            DRAGONSTONE_NECKLACE.id,
            DRAGONSTONE_BRACELET.id,
            DRAGONSTONE_AMULET.id);
      case LEVEL_6:
        return ImmutableSet.of(ONYX_RING.id, ONYX_NECKLACE.id, ONYX_BRACELET.id, ONYX_AMULET.id);
      case LEVEL_7:
        return ImmutableSet.of(
            ZENYTE_RING.id, ZENYTE_NECKLACE.id, ZENYTE_BRACELET.id, ZENYTE_AMULET.id);
      default:
    }

    return null;
  }
}
