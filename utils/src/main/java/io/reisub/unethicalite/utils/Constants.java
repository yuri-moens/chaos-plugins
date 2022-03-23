package io.reisub.unethicalite.utils;

import com.google.common.collect.ImmutableSet;
import net.runelite.api.ItemID;
import net.runelite.api.NullObjectID;
import net.runelite.api.ObjectID;

import java.util.Set;

public class Constants {
    public static final int MAKE_FIRST_ITEM_WIDGET_ID = 17694734;

    public static final Set<Integer> LOG_IDS = ImmutableSet.of(
            ItemID.LOGS,
            ItemID.OAK_LOGS,
            ItemID.WILLOW_LOGS,
            ItemID.TEAK_LOGS,
            ItemID.MAPLE_LOGS,
            ItemID.MAHOGANY_LOGS,
            ItemID.YEW_LOGS,
            ItemID.MAGIC_LOGS,
            ItemID.REDWOOD_LOGS
    );

    public static final Set<Integer> DIGSITE_PENDANT_IDS = ImmutableSet.of(
            ItemID.DIGSITE_PENDANT_1,
            ItemID.DIGSITE_PENDANT_2,
            ItemID.DIGSITE_PENDANT_3,
            ItemID.DIGSITE_PENDANT_4,
            ItemID.DIGSITE_PENDANT_5
    );

    public static final int BIRD_HOUSE_EMPTY_SPACE = ObjectID.SPACE;

    public static final int MEADOW_NORTH_SPACE = NullObjectID.NULL_30565;
    public static final int MEADOW_SOUTH_SPACE = NullObjectID.NULL_30566;
    public static final int VERDANT_NORTH_SPACE = NullObjectID.NULL_30567;
    public static final int VERDANT_SOUTH_SPACE = NullObjectID.NULL_30568;

    public static final Set<Integer> BIRD_HOUSE_SPACES = ImmutableSet.of(
            MEADOW_NORTH_SPACE,
            MEADOW_SOUTH_SPACE,
            VERDANT_NORTH_SPACE,
            VERDANT_SOUTH_SPACE
    );

    public static final Set<Integer> BIRD_HOUSE_IDS = ImmutableSet.of(
            ObjectID.BIRDHOUSE,
            ObjectID.BIRDHOUSE_30555,
            ObjectID.OAK_BIRDHOUSE,
            ObjectID.OAK_BIRDHOUSE_30558,
            ObjectID.WILLOW_BIRDHOUSE,
            ObjectID.WILLOW_BIRDHOUSE_30561,
            ObjectID.TEAK_BIRDHOUSE,
            ObjectID.TEAK_BIRDHOUSE_30564,
            ObjectID.MAPLE_BIRDHOUSE,
            ObjectID.MAPLE_BIRDHOUSE_31829,
            ObjectID.MAHOGANY_BIRDHOUSE,
            ObjectID.MAHOGANY_BIRDHOUSE_31832,
            ObjectID.YEW_BIRDHOUSE,
            ObjectID.YEW_BIRDHOUSE_31835,
            ObjectID.MAGIC_BIRDHOUSE,
            ObjectID.MAGIC_BIRDHOUSE_31838,
            ObjectID.REDWOOD_BIRDHOUSE,
            ObjectID.REDWOOD_BIRDHOUSE_31841
    );

    public static final Set<Integer> BIRD_HOUSE_EMPTY_IDS = ImmutableSet.of(
            ObjectID.BIRDHOUSE_EMPTY,
            ObjectID.OAK_BIRDHOUSE_EMPTY,
            ObjectID.WILLOW_BIRDHOUSE_EMPTY,
            ObjectID.TEAK_BIRDHOUSE_EMPTY,
            ObjectID.MAPLE_BIRDHOUSE_EMPTY,
            ObjectID.MAHOGANY_BIRDHOUSE_EMPTY,
            ObjectID.YEW_BIRDHOUSE_EMPTY,
            ObjectID.MAGIC_BIRDHOUSE_EMPTY,
            ObjectID.REDWOOD_BIRDHOUSE_EMPTY
    );

    public static final Set<Integer> BIRD_HOUSE_ITEM_IDS = ImmutableSet.of(
            ItemID.BIRD_HOUSE,
            ItemID.OAK_BIRD_HOUSE,
            ItemID.WILLOW_BIRD_HOUSE,
            ItemID.TEAK_BIRD_HOUSE,
            ItemID.MAPLE_BIRD_HOUSE,
            ItemID.MAHOGANY_BIRD_HOUSE,
            ItemID.YEW_BIRD_HOUSE,
            ItemID.MAGIC_BIRD_HOUSE,
            ItemID.REDWOOD_BIRD_HOUSE
    );

    public static final Set<Integer> BIRD_HOUSE_SEED_IDS = ImmutableSet.of(
            ItemID.BARLEY_SEED,
            ItemID.HAMMERSTONE_SEED,
            ItemID.ASGARNIAN_SEED,
            ItemID.JUTE_SEED,
            ItemID.YANILLIAN_SEED,
            ItemID.KRANDORIAN_SEED
    );

    public static final Set<Integer> BIRD_NEST_IDS = ImmutableSet.of(
            ItemID.BIRD_NEST,
            ItemID.BIRD_NEST_5071,
            ItemID.BIRD_NEST_5072,
            ItemID.BIRD_NEST_5073,
            ItemID.BIRD_NEST_5074,
            ItemID.BIRD_NEST_5075,
            ItemID.BIRD_NEST_7413,
            ItemID.BIRD_NEST_13653,
            ItemID.BIRD_NEST_22798,
            ItemID.BIRD_NEST_22800,
            ItemID.CLUE_NEST_EASY,
            ItemID.CLUE_NEST_MEDIUM,
            ItemID.CLUE_NEST_HARD,
            ItemID.CLUE_NEST_ELITE
    );

    public static final Set<Integer> MAGIC_MUSHTREE_IDS = ImmutableSet.of(
            ObjectID.MAGIC_MUSHTREE,
            ObjectID.MAGIC_MUSHTREE_30922,
            ObjectID.MAGIC_MUSHTREE_30924
    );

    public static final Set<Integer> ESSENCE_IDS = ImmutableSet.of(
            ItemID.RUNE_ESSENCE,
            ItemID.PURE_ESSENCE,
            ItemID.DAEYALT_ESSENCE
    );

    public static final Set<Integer> ANVIL_IDS = ImmutableSet.of(
            ObjectID.ANVIL,
            ObjectID.ANVIL_2097,
            ObjectID.AN_EXPERIMENTAL_ANVIL,
            ObjectID.ANVIL_4306,
            ObjectID.ANVIL_6150,
            ObjectID.ANVIL_22725,
            ObjectID.BARBARIAN_ANVIL,
            ObjectID.ANVIL_28563,
            ObjectID.ORNAMENTAL_ANVIL,
            ObjectID.ORNAMENTAL_ANVIL_29310,
            ObjectID.GIANT_ANVIL,
            ObjectID.ANVIL_31623,
            ObjectID.ANVIL_32215,
            ObjectID.ANVIL_32216,
            ObjectID.ANVIL_39242,
            ObjectID.RUSTED_ANVIL,
            ObjectID.GIANT_ANVIL_39724,
            ObjectID.ANVIL_40725,
            ObjectID.ANVIL_42825,
            ObjectID.ANVIL_42860
    );

    public static final Set<Integer> STAMINA_POTION_IDS = ImmutableSet.of(
            ItemID.STAMINA_POTION1,
            ItemID.STAMINA_POTION2,
            ItemID.STAMINA_POTION3,
            ItemID.STAMINA_POTION4
    );

    public static final Set<Integer> PRAYER_RESTORE_POTION_IDS = ImmutableSet.of(
            ItemID.PRAYER_POTION1,
            ItemID.PRAYER_POTION2,
            ItemID.PRAYER_POTION3,
            ItemID.PRAYER_POTION4,
            ItemID.PRAYER_MIX1,
            ItemID.PRAYER_MIX2,
            ItemID.SUPER_RESTORE1,
            ItemID.SUPER_RESTORE2,
            ItemID.SUPER_RESTORE3,
            ItemID.SUPER_RESTORE4,
            ItemID.SUPER_RESTORE_MIX1,
            ItemID.SUPER_RESTORE_MIX2
    );

    public static final Set<Integer> MINEABLE_GEM_IDS = ImmutableSet.of(
            ItemID.UNCUT_SAPPHIRE,
            ItemID.UNCUT_EMERALD,
            ItemID.UNCUT_RUBY,
            ItemID.UNCUT_DIAMOND
    );

    public static final Set<Integer> ESSENCE_POUCH_IDS = ImmutableSet.of(
            ItemID.GIANT_POUCH,
            ItemID.GIANT_POUCH_5515,
            ItemID.LARGE_POUCH,
            ItemID.LARGE_POUCH_5513,
            ItemID.MEDIUM_POUCH,
            ItemID.MEDIUM_POUCH_5511,
            ItemID.SMALL_POUCH
    );

    public static final Set<Integer> DEGRADED_ESSENCE_POUCH_IDS = ImmutableSet.of(
            ItemID.GIANT_POUCH_5515,
            ItemID.LARGE_POUCH_5513,
            ItemID.MEDIUM_POUCH_5511
    );

    public static final Set<Integer> EXPLORERS_RING_IDS = ImmutableSet.of(
            ItemID.EXPLORERS_RING_2,
            ItemID.EXPLORERS_RING_3,
            ItemID.EXPLORERS_RING_4
    );

    public static final Set<Integer> HERB_PATCH_IDS = ImmutableSet.of(

    );
}
