package io.reisub.unethicalite.farming;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.reisub.unethicalite.utils.Constants;
import java.util.Map;
import java.util.Set;
import net.runelite.api.ItemID;

public class OneClick {
  public static final String ONE_CLICK_FARMING = "One Click Farming";
  public static final Map<Integer, Set<Integer>> ONE_CLICK_GAME_OBJECTS_MAP;
  public static final Map<Integer, Set<Integer>> ONE_CLICK_ITEMS_MAP;
  public static final Map<Integer, Set<Integer>> ONE_CLICK_NPCS_MAP;

  static {
    ImmutableMap.Builder<Integer, Set<Integer>> builder = ImmutableMap.builder();

    for (int i : Constants.ALLOTMENT_SEED_IDS) {
      builder.put(i, Constants.ALLOTMENT_PATCH_IDS);
    }

    for (int i : Constants.FLOWER_SEED_IDS) {
      builder.put(i, Constants.FLOWER_PATCH_IDS);
    }

    for (int i : Constants.HERB_SEED_IDS) {
      builder.put(i, Constants.HERB_PATCH_IDS);
    }

    for (int i : Constants.HOPS_SEED_IDS) {
      builder.put(i, Constants.HOPS_PATCH_IDS);
    }

    for (int i : Constants.BUSH_SEED_IDS) {
      builder.put(i, Constants.BUSH_PATCH_IDS);
    }

    for (int i : Constants.TREE_SAPLING_IDS) {
      builder.put(i, Constants.TREE_PATCH_IDS);
    }

    for (int i : Constants.FRUIT_TREE_SAPLING_IDS) {
      builder.put(i, Constants.FRUIT_TREE_PATCH_IDS);
    }

    for (int i : Constants.HARDWOOD_TREE_SAPLING_IDS) {
      builder.put(i, Constants.HARDWOOD_TREE_PATCH_IDS);
    }

    for (int i : Constants.CACTUS_SEED_IDS) {
      builder.put(i, Constants.CACTUS_PATCH_IDS);
    }

    for (int i : Constants.ANIMA_SEED_IDS) {
      builder.put(i, Constants.ANIMA_PATCH_IDS);
    }

    builder.put(ItemID.SPIRIT_SAPLING, Constants.SPIRIT_TREE_PATCH_IDS);
    builder.put(ItemID.SEAWEED_SPORE, Constants.SEAWEED_PATCH_IDS);
    builder.put(ItemID.GRAPE_SEED, Constants.GRAPE_PATCH_IDS);
    builder.put(ItemID.MUSHROOM_SPORE, Constants.MUSHROOM_PATCH_IDS);
    builder.put(ItemID.BELLADONNA_SEED, Constants.BELLADONNA_PATCH_IDS);
    builder.put(ItemID.HESPORI_SEED, Constants.HESPORI_PATCH_IDS);
    builder.put(ItemID.CALQUAT_SAPLING, Constants.CALQUAT_PATCH_IDS);
    builder.put(ItemID.CRYSTAL_SEED, Constants.CRYSTAL_PATCH_IDS);
    builder.put(ItemID.CELASTRUS_SAPLING, Constants.CELASTRUS_PATCH_IDS);
    builder.put(ItemID.REDWOOD_SAPLING, Constants.REDWOOD_PATCH_IDS);

    Set<Integer> allPatches =
        new ImmutableSet.Builder<Integer>()
            .addAll(Constants.ALLOTMENT_PATCH_IDS)
            .addAll(Constants.FLOWER_PATCH_IDS)
            .addAll(Constants.HERB_PATCH_IDS)
            .addAll(Constants.HOPS_PATCH_IDS)
            .addAll(Constants.BUSH_PATCH_IDS)
            .addAll(Constants.TREE_PATCH_IDS)
            .addAll(Constants.FRUIT_TREE_PATCH_IDS)
            .addAll(Constants.HARDWOOD_TREE_PATCH_IDS)
            .addAll(Constants.CACTUS_PATCH_IDS)
            .addAll(Constants.ANIMA_PATCH_IDS)
            .addAll(Constants.SPIRIT_TREE_PATCH_IDS)
            .addAll(Constants.SEAWEED_PATCH_IDS)
            .addAll(Constants.GRAPE_PATCH_IDS)
            .addAll(Constants.MUSHROOM_PATCH_IDS)
            .addAll(Constants.BELLADONNA_PATCH_IDS)
            .addAll(Constants.HESPORI_PATCH_IDS)
            .addAll(Constants.CALQUAT_PATCH_IDS)
            .addAll(Constants.CRYSTAL_PATCH_IDS)
            .addAll(Constants.CELASTRUS_PATCH_IDS)
            .addAll(Constants.REDWOOD_PATCH_IDS)
            .build();

    for (int i : Constants.COMPOST_IDS) {
      builder.put(i, allPatches);
    }

    ONE_CLICK_GAME_OBJECTS_MAP = builder.build();

    builder = ImmutableMap.builder();

    for (int i : Constants.TREE_SEED_IDS) {
      builder.put(i, ImmutableSet.of(ItemID.FILLED_PLANT_POT));
    }

    for (int i : Constants.TREE_SEEDLING_IDS) {
      builder.put(i, Constants.WATERING_CAN_IDS);
    }

    for (int i : Constants.WATERING_CAN_IDS) {
      builder.put(i, Constants.TREE_SEEDLING_IDS);
    }

    ONE_CLICK_ITEMS_MAP = builder.build();

    builder = ImmutableMap.builder();

    for (int i : Constants.NOTABLE_PRODUCE_IDS) {
      builder.put(i, Constants.TOOL_LEPRECHAUN_IDS);
    }

    ONE_CLICK_NPCS_MAP = builder.build();
  }
}
