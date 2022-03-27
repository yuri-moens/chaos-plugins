package io.reisub.unethicalite.utils;

import com.google.common.collect.ImmutableSet;
import net.runelite.api.ItemID;
import net.runelite.api.NpcID;
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

    public static final Set<Integer> ARDOUGNE_CLOAK_IDS = ImmutableSet.of(
            ItemID.ARDOUGNE_CLOAK_1,
            ItemID.ARDOUGNE_CLOAK_2,
            ItemID.ARDOUGNE_CLOAK_3,
            ItemID.ARDOUGNE_CLOAK_4,
            ItemID.ARDOUGNE_CLOAK,
            ItemID.ARDOUGNE_MAX_CAPE
    );

    public static final Set<Integer> DUELING_RING_IDS = ImmutableSet.of(
            ItemID.RING_OF_DUELING1,
            ItemID.RING_OF_DUELING2,
            ItemID.RING_OF_DUELING3,
            ItemID.RING_OF_DUELING4,
            ItemID.RING_OF_DUELING5,
            ItemID.RING_OF_DUELING6,
            ItemID.RING_OF_DUELING7,
            ItemID.RING_OF_DUELING8
    );

    public static final Set<Integer> EXPLORERS_RING_IDS = ImmutableSet.of(
            ItemID.EXPLORERS_RING_2,
            ItemID.EXPLORERS_RING_3,
            ItemID.EXPLORERS_RING_4
    );

    public static final Set<Integer> AMULET_OF_GLORY_IDS = ImmutableSet.of(
            ItemID.AMULET_OF_GLORY1,
            ItemID.AMULET_OF_GLORY2,
            ItemID.AMULET_OF_GLORY3,
            ItemID.AMULET_OF_GLORY4,
            ItemID.AMULET_OF_GLORY5,
            ItemID.AMULET_OF_GLORY6,
            ItemID.AMULET_OF_GLORY_T1,
            ItemID.AMULET_OF_GLORY_T2,
            ItemID.AMULET_OF_GLORY_T3,
            ItemID.AMULET_OF_GLORY_T4,
            ItemID.AMULET_OF_GLORY_T5,
            ItemID.AMULET_OF_GLORY_T6,
            ItemID.AMULET_OF_ETERNAL_GLORY
    );

    public static final Set<Integer> SKILL_NECKLACE_IDS = ImmutableSet.of(
            ItemID.SKILLS_NECKLACE1,
            ItemID.SKILLS_NECKLACE2,
            ItemID.SKILLS_NECKLACE3,
            ItemID.SKILLS_NECKLACE4,
            ItemID.SKILLS_NECKLACE5,
            ItemID.SKILLS_NECKLACE6
    );

    public static final Set<Integer> HERB_PATCH_IDS = ImmutableSet.of(

    );

    public static final Set<Integer> BANK_OBJECT_IDS = ImmutableSet.of(
            ObjectID.BANK_BOOTH,
            ObjectID.BANK_BOOTH_10083,
            ObjectID.BANK_BOOTH_10355,
            ObjectID.BANK_BOOTH_10357,
            ObjectID.BANK_BOOTH_10517,
            ObjectID.BANK_BOOTH_10527,
            ObjectID.BANK_BOOTH_10583,
            ObjectID.BANK_BOOTH_10584,
            ObjectID.BANK_BOOTH_11338,
            ObjectID.BANK_BOOTH_12798,
            ObjectID.BANK_BOOTH_12799,
            ObjectID.BANK_BOOTH_12800,
            ObjectID.BANK_BOOTH_12801,
            ObjectID.BANK_BOOTH_14367,
            ObjectID.BANK_BOOTH_14368,
            ObjectID.BANK_BOOTH_16642,
            ObjectID.BANK_BOOTH_16700,
            ObjectID.BANK_BOOTH_18491,
            ObjectID.PRIVATE_BANK_BOOTH,
            ObjectID.PRIVATE_BANK_BOOTH_20324,
            ObjectID.BANK_BOOTH_20325,
            ObjectID.BANK_BOOTH_20326,
            ObjectID.BANK_BOOTH_20327,
            ObjectID.BANK_BOOTH_20328,
            ObjectID.BANK_BOOTH_22819,
            ObjectID.BANK_BOOTH_24101,
            ObjectID.BANK_BOOTH_24347,
            ObjectID.BANK_BOOTH_25808,
            ObjectID.BANK_BOOTH_27254,
            ObjectID.BANK_BOOTH_27260,
            ObjectID.BANK_BOOTH_27263,
            ObjectID.BANK_BOOTH_27265,
            ObjectID.BANK_BOOTH_27267,
            ObjectID.BANK_BOOTH_27292,
            ObjectID.BANK_BOOTH_27718,
            ObjectID.BANK_BOOTH_27719,
            ObjectID.BANK_BOOTH_27720,
            ObjectID.BANK_BOOTH_27721,
            ObjectID.BANK_BOOTH_28429,
            ObjectID.BANK_BOOTH_28430,
            ObjectID.BANK_BOOTH_28431,
            ObjectID.BANK_BOOTH_28432,
            ObjectID.BANK_BOOTH_28433,
            ObjectID.BANK_BOOTH_28546,
            ObjectID.BANK_BOOTH_28547,
            ObjectID.BANK_BOOTH_28548,
            ObjectID.BANK_BOOTH_28549,
            ObjectID.BANK_BOOTH_32666,
            NullObjectID.NULL_34810,
            ObjectID.BANK_BOOTH_36559,
            ObjectID.BANK_BOOTH_37959,
            ObjectID.BANK_BOOTH_39238,
            ObjectID.BANK_BOOTH_42837,
            ObjectID.BANK_CHEST,
            ObjectID.BANK_CHEST_4483,
            ObjectID.BANK_CHEST_10562,
            ObjectID.BANK_CHEST_14382,
            ObjectID.BANK_CHEST_14886,
            ObjectID.BANK_CHEST_16695,
            ObjectID.BANK_CHEST_16696,
            ObjectID.BANK_CHEST_19051,
            ObjectID.BANK_CHEST_21301,
            ObjectID.BANK_CHEST_26707,
            ObjectID.BANK_CHEST_26711,
            ObjectID.BANK_CHEST_28594,
            ObjectID.BANK_CHEST_28595,
            ObjectID.BANK_CHEST_28816,
            ObjectID.BANK_CHEST_28861,
            ObjectID.BANK_CHEST_29321,
            ObjectID.BANK_CHEST_30087,
            ObjectID.BANK_CHEST_30267,
            ObjectID.BANK_CHESTWRECK,
            ObjectID.BANK_CHEST_30926,
            ObjectID.BANK_CHEST_30989,
            ObjectID.BANK_CHEST_34343,
            ObjectID.BANK_CHEST_40473,
            ObjectID.BANK_CHEST_41315,
            ObjectID.BANK_CHEST_41493
    );

    public static final Set<Integer> BANK_NPC_IDS = ImmutableSet.of(
            NpcID.BANKER,
            NpcID.BANKER_1479,
            NpcID.BANKER_1480,
            NpcID.BANKER_1613,
            NpcID.BANKER_1618,
            NpcID.BANKER_1633,
            NpcID.BANKER_1634,
            NpcID.BANKER_2117,
            NpcID.BANKER_2118,
            NpcID.BANKER_2119,
            NpcID.BANKER_2292,
            NpcID.BANKER_2293,
            NpcID.BANKER_2368,
            NpcID.BANKER_2369,
            NpcID.BANKER_2633,
            NpcID.BANKER_2897,
            NpcID.BANKER_2898,
            NpcID.GHOST_BANKER,
            NpcID.BANKER_3089,
            NpcID.BANKER_3090,
            NpcID.BANKER_3091,
            NpcID.BANKER_3092,
            NpcID.BANKER_3093,
            NpcID.BANKER_3094,
            NpcID.BANKER_TUTOR,
            NpcID.BANKER_3318,
            NpcID.SIRSAL_BANKER,
            NpcID.BANKER_3887,
            NpcID.BANKER_3888,
            NpcID.BANKER_4054,
            NpcID.BANKER_4055,
            NpcID.NARDAH_BANKER,
            NpcID.GNOME_BANKER,
            NpcID.BANKER_6859,
            NpcID.BANKER_6860,
            NpcID.BANKER_6861,
            NpcID.BANKER_6862,
            NpcID.BANKER_6863,
            NpcID.BANKER_6864,
            NpcID.BANKER_6939,
            NpcID.BANKER_6940,
            NpcID.BANKER_6941,
            NpcID.BANKER_6942,
            NpcID.BANKER_6969,
            NpcID.BANKER_6970,
            NpcID.BANKER_7057,
            NpcID.BANKER_7058,
            NpcID.BANKER_7059,
            NpcID.BANKER_7060,
            NpcID.BANKER_7077,
            NpcID.BANKER_7078,
            NpcID.BANKER_7079,
            NpcID.BANKER_7080,
            NpcID.BANKER_7081,
            NpcID.BANKER_7082,
            NpcID.BANKER_8321,
            NpcID.BANKER_8322,
            NpcID.BANKER_8589,
            NpcID.BANKER_8590,
            NpcID.BANKER_8666,
            NpcID.BANKER_9127,
            NpcID.BANKER_9128,
            NpcID.BANKER_9129,
            NpcID.BANKER_9130,
            NpcID.BANKER_9131,
            NpcID.BANKER_9132,
            NpcID.BANKER_9484,
            NpcID.BANKER_9718,
            NpcID.BANKER_9719,
            NpcID.BANKER_10389,
            NpcID.BANKER_10734,
            NpcID.BANKER_10735,
            NpcID.BANKER_10736,
            NpcID.BANKER_10737
    );
}
