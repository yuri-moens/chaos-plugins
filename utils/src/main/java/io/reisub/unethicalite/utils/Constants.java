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

    public static final Set<Integer> BIRDHOUSE_SPACES = ImmutableSet.of(
        NullObjectID.NULL_30567, // verdant north
        NullObjectID.NULL_30568, // verdant south
        NullObjectID.NULL_30565, // meadow north
        NullObjectID.NULL_30566 //meadow south
    );

    public static final Set<Integer> BIRDHOUSE_IDS = ImmutableSet.of(
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

    public static final Set<Integer> BIRDHOUSE_EMPTY_IDS = ImmutableSet.of(
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

    public static final Set<Integer> BIRDHOUSE_ITEM_IDS = ImmutableSet.of(
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

    public static final Set<Integer> BIRDHOUSE_SEED_IDS = ImmutableSet.of(
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
}
