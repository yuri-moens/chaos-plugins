package io.reisub.unethicalite.farming;

import com.google.common.collect.ImmutableMap;
import io.reisub.unethicalite.utils.Constants;
import net.runelite.api.ItemID;

import java.util.Map;
import java.util.Set;

public class OneClick {
    public static final String ONE_CLICK_FARMING = "One Click Farming";
    public static final Map<Integer, Set<Integer>> ONE_CLICK_MAP;

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

        ONE_CLICK_MAP = builder.build();
    }
}
