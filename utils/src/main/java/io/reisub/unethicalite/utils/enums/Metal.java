package io.reisub.unethicalite.utils.enums;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.ItemID;

@AllArgsConstructor
@Getter
public enum Metal {
  BRONZE(
      ItemID.BRONZE_BAR,
      ImmutableMap.of(ItemID.TIN_ORE, 1, ItemID.COPPER_ORE, 1),
      ImmutableMap.of(ItemID.TIN_ORE, 1, ItemID.COPPER_ORE, 1)),
  IRON(ItemID.IRON_BAR, ImmutableMap.of(ItemID.IRON_ORE, 1), ImmutableMap.of(ItemID.IRON_ORE, 1)),
  STEEL(
      ItemID.STEEL_BAR,
      ImmutableMap.of(ItemID.IRON_ORE, 1, ItemID.COAL, 2),
      ImmutableMap.of(ItemID.IRON_ORE, 1, ItemID.COAL, 1)),
  GOLD(ItemID.GOLD_BAR, ImmutableMap.of(ItemID.GOLD_ORE, 1), ImmutableMap.of(ItemID.GOLD_ORE, 1)),
  MITHRIL(
      ItemID.MITHRIL_BAR,
      ImmutableMap.of(ItemID.MITHRIL_ORE, 1, ItemID.COAL, 4),
      ImmutableMap.of(ItemID.MITHRIL_ORE, 1, ItemID.COAL, 2)),
  ADAMANTITE(
      ItemID.ADAMANTITE_BAR,
      ImmutableMap.of(ItemID.ADAMANTITE_ORE, 1, ItemID.COAL, 6),
      ImmutableMap.of(ItemID.ADAMANTITE_ORE, 1, ItemID.COAL, 3)),
  RUNITE(
      ItemID.RUNITE_BAR,
      ImmutableMap.of(ItemID.RUNITE_ORE, 1, ItemID.COAL, 8),
      ImmutableMap.of(ItemID.RUNITE_ORE, 1, ItemID.COAL, 4));

  private final int barId;
  private final Map<Integer, Integer> materials;
  private final Map<Integer, Integer> blastFurnaceMaterials;
}
