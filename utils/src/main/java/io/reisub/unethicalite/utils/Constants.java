package io.reisub.unethicalite.utils;

import com.google.common.collect.ImmutableSet;
import dev.hoot.api.widgets.Widgets;
import net.runelite.api.ItemID;
import net.runelite.api.NpcID;
import net.runelite.api.NullObjectID;
import net.runelite.api.ObjectID;
import net.runelite.api.widgets.Widget;

import java.util.Set;
import java.util.function.Supplier;

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

    public static final Set<Integer> COMPOST_BIN_IDS = ImmutableSet.of(
            NullObjectID.NULL_7836,
            NullObjectID.NULL_7837,
            NullObjectID.NULL_7838,
            NullObjectID.NULL_7839,
            NullObjectID.NULL_27112,
            NullObjectID.NULL_34631
    );

    public static final Set<Integer> NOTABLE_PRODUCE_IDS = ImmutableSet.of(
            ItemID.POTATO,
            ItemID.ONION,
            ItemID.TOMATO,
            ItemID.SWEETCORN,
            ItemID.STRAWBERRY,
            ItemID.WATERMELON,
            ItemID.SNAPE_GRASS,
            ItemID.MARIGOLDS,
            ItemID.ROSEMARY,
            ItemID.NASTURTIUMS,
            ItemID.WOAD_LEAF,
            ItemID.LIMPWURT_ROOT,
            ItemID.WHITE_LILY,
            ItemID.GRIMY_GUAM_LEAF,
            ItemID.GRIMY_MARRENTILL,
            ItemID.GRIMY_TARROMIN,
            ItemID.GRIMY_HARRALANDER,
            ItemID.GRIMY_RANARR_WEED,
            ItemID.GRIMY_TOADFLAX,
            ItemID.GRIMY_IRIT_LEAF,
            ItemID.GRIMY_AVANTOE,
            ItemID.GRIMY_KWUARM,
            ItemID.GRIMY_SNAPDRAGON,
            ItemID.GRIMY_CADANTINE,
            ItemID.GRIMY_LANTADYME,
            ItemID.GRIMY_DWARF_WEED,
            ItemID.GRIMY_TORSTOL,
            ItemID.BARLEY,
            ItemID.HAMMERSTONE_HOPS,
            ItemID.ASGARNIAN_HOPS,
            ItemID.JUTE_FIBRE,
            ItemID.YANILLIAN_HOPS,
            ItemID.KRANDORIAN_HOPS,
            ItemID.WILDBLOOD_HOPS,
            ItemID.REDBERRIES,
            ItemID.CADAVA_BERRIES,
            ItemID.DWELLBERRIES,
            ItemID.JANGERBERRIES,
            ItemID.WHITE_BERRIES,
            ItemID.POISON_IVY_BERRIES,
            ItemID.COOKING_APPLE,
            ItemID.BANANA,
            ItemID.ORANGE,
            ItemID.CURRY_LEAF,
            ItemID.PINEAPPLE,
            ItemID.PAPAYA_FRUIT,
            ItemID.COCONUT,
            ItemID.DRAGONFRUIT,
            ItemID.GIANT_SEAWEED,
            ItemID.GRAPES,
            ItemID.MUSHROOM,
            ItemID.CACTUS_SPINE,
            ItemID.POTATO_CACTUS
    );

    public static final Set<Integer> TOOL_LEPRECHAUN_IDS = ImmutableSet.of(
            NpcID.TOOL_LEPRECHAUN,
            NpcID.TOOL_LEPRECHAUN_757,
            NpcID.TOOL_LEPRECHAUN_7757
    );

    public static final Set<Integer> ALLOTMENT_PATCH_IDS = ImmutableSet.of(
            NullObjectID.NULL_8550,
            NullObjectID.NULL_8551,
            NullObjectID.NULL_8552,
            NullObjectID.NULL_8553,
            NullObjectID.NULL_8554,
            NullObjectID.NULL_8555,
            NullObjectID.NULL_8556,
            NullObjectID.NULL_8557,
            NullObjectID.NULL_21950,
            NullObjectID.NULL_27113,
            NullObjectID.NULL_27114,
            NullObjectID.NULL_33693,
            NullObjectID.NULL_33694
    );

    public static final Set<Integer> FLOWER_PATCH_IDS = ImmutableSet.of(
            NullObjectID.NULL_7847,
            NullObjectID.NULL_7848,
            NullObjectID.NULL_7849,
            NullObjectID.NULL_7850,
            NullObjectID.NULL_27111,
            NullObjectID.NULL_33649
    );

    public static final Set<Integer> HERB_PATCH_IDS = ImmutableSet.of(
            NullObjectID.NULL_8150,
            NullObjectID.NULL_8151,
            NullObjectID.NULL_8152,
            NullObjectID.NULL_8153,
            NullObjectID.NULL_9372,
            NullObjectID.NULL_18816,
            NullObjectID.NULL_27115,
            NullObjectID.NULL_33176,
            NullObjectID.NULL_33979
    );

    public static final Set<Integer> HOPS_PATCH_IDS = ImmutableSet.of(

    );

    public static final Set<Integer> BUSH_PATCH_IDS = ImmutableSet.of(
            NullObjectID.NULL_34006
    );

    public static final Set<Integer> TREE_PATCH_IDS = ImmutableSet.of(
            NullObjectID.NULL_33732
    );

    public static final Set<Integer> FRUIT_TREE_PATCH_IDS = ImmutableSet.of(
            NullObjectID.NULL_34007
    );

    public static final Set<Integer> HARDWOOD_TREE_PATCH_IDS = ImmutableSet.of(

    );

    public static final Set<Integer> SPIRIT_TREE_PATCH_IDS = ImmutableSet.of(
            NullObjectID.NULL_33733
    );

    public static final Set<Integer> SEAWEED_PATCH_IDS = ImmutableSet.of(

    );

    public static final Set<Integer> CACTUS_PATCH_IDS = ImmutableSet.of(
            NullObjectID.NULL_33761
    );

    public static final Set<Integer> GRAPE_PATCH_IDS = ImmutableSet.of(

    );

    public static final Set<Integer> MUSHROOM_PATCH_IDS = ImmutableSet.of(

    );

    public static final Set<Integer> BELLADONNA_PATCH_IDS = ImmutableSet.of(

    );

    public static final Set<Integer> HESPORI_PATCH_IDS = ImmutableSet.of(
            NullObjectID.NULL_34630
    );

    public static final Set<Integer> ANIMA_PATCH_IDS = ImmutableSet.of(
            NullObjectID.NULL_33998
    );

    public static final Set<Integer> CALQUAT_PATCH_IDS = ImmutableSet.of(

    );

    public static final Set<Integer> CRYSTAL_PATCH_IDS = ImmutableSet.of(

    );

    public static final Set<Integer> CELASTRUS_PATCH_IDS = ImmutableSet.of(
            NullObjectID.NULL_34629
    );

    public static final Set<Integer> REDWOOD_PATCH_IDS = ImmutableSet.of(
            NullObjectID.NULL_34055
    );

    public static final Set<Integer> ALLOTMENT_SEED_IDS = ImmutableSet.of(
            ItemID.POTATO_SEED,
            ItemID.ONION_SEED,
            ItemID.CABBAGE_SEED,
            ItemID.TOMATO_SEED,
            ItemID.SWEETCORN_SEED,
            ItemID.STRAWBERRY_SEED,
            ItemID.WATERMELON_SEED,
            ItemID.SNAPE_GRASS_SEED
    );

    public static final Set<Integer> FLOWER_SEED_IDS = ImmutableSet.of(
            ItemID.MARIGOLD_SEED,
            ItemID.ROSEMARY_SEED,
            ItemID.NASTURTIUM_SEED,
            ItemID.WOAD_SEED,
            ItemID.LIMPWURT_SEED,
            ItemID.WHITE_LILY_SEED
    );

    public static final Set<Integer> HERB_SEED_IDS = ImmutableSet.of(
            ItemID.GUAM_SEED,
            ItemID.MARRENTILL_SEED,
            ItemID.TARROMIN_SEED,
            ItemID.HARRALANDER_SEED,
            ItemID.RANARR_SEED,
            ItemID.TOADFLAX_SEED,
            ItemID.IRIT_SEED,
            ItemID.AVANTOE_SEED,
            ItemID.KWUARM_SEED,
            ItemID.SNAPDRAGON_SEED,
            ItemID.CADANTINE_SEED,
            ItemID.LANTADYME_SEED,
            ItemID.DWARF_WEED_SEED,
            ItemID.TORSTOL_SEED
    );

    public static final Set<Integer> HOPS_SEED_IDS = ImmutableSet.of(
            ItemID.BARLEY_SEED,
            ItemID.HAMMERSTONE_SEED,
            ItemID.ASGARNIAN_SEED,
            ItemID.JUTE_SEED,
            ItemID.YANILLIAN_SEED,
            ItemID.KRANDORIAN_SEED,
            ItemID.WILDBLOOD_SEED
    );

    public static final Set<Integer> BUSH_SEED_IDS = ImmutableSet.of(
            ItemID.REDBERRY_SEED,
            ItemID.CADAVABERRY_SEED,
            ItemID.DWELLBERRY_SEED,
            ItemID.JANGERBERRY_SEED,
            ItemID.WHITEBERRY_SEED,
            ItemID.POISON_IVY_SEED
    );

    public static final Set<Integer> TREE_SEED_IDS = ImmutableSet.of(
            ItemID.ACORN,
            ItemID.WILLOW_SEED,
            ItemID.MAPLE_SEED,
            ItemID.YEW_SEED,
            ItemID.MAGIC_SEED,
            ItemID.APPLE_TREE_SEED,
            ItemID.BANANA_TREE_SEED,
            ItemID.ORANGE_TREE_SEED,
            ItemID.CURRY_TREE_SEED,
            ItemID.PINEAPPLE_SEED,
            ItemID.PAPAYA_TREE_SEED,
            ItemID.DRAGONFRUIT_TREE_SEED,
            ItemID.TEAK_SEED,
            ItemID.MAHOGANY_SEED,
            ItemID.CALQUAT_TREE_SEED,
            ItemID.CRYSTAL_SEED,
            ItemID.SPIRIT_SEED,
            ItemID.CELASTRUS_SEED,
            ItemID.REDWOOD_TREE_SEED
    );

    public static final Set<Integer> TREE_SEEDLING_IDS = ImmutableSet.of(
            ItemID.OAK_SEEDLING,
            ItemID.WILLOW_SEEDLING,
            ItemID.MAPLE_SEEDLING,
            ItemID.YEW_SEEDLING,
            ItemID.MAGIC_SEEDLING,
            ItemID.APPLE_SEEDLING,
            ItemID.BANANA_SEEDLING,
            ItemID.ORANGE_SEEDLING,
            ItemID.CURRY_SEEDLING,
            ItemID.PINEAPPLE_SEEDLING,
            ItemID.PAPAYA_SEEDLING,
            ItemID.DRAGONFRUIT_SEEDLING,
            ItemID.TEAK_SEEDLING,
            ItemID.MAHOGANY_SEEDLING,
            ItemID.CALQUAT_SEEDLING,
            ItemID.CRYSTAL_SEEDLING,
            ItemID.SPIRIT_SEEDLING,
            ItemID.CELASTRUS_SEEDLING,
            ItemID.REDWOOD_SEEDLING
    );

    public static final Set<Integer> TREE_SAPLING_IDS = ImmutableSet.of(
            ItemID.OAK_SAPLING,
            ItemID.WILLOW_SAPLING,
            ItemID.MAPLE_SAPLING,
            ItemID.YEW_SAPLING,
            ItemID.MAGIC_SAPLING
    );

    public static final Set<Integer> FRUIT_TREE_SAPLING_IDS = ImmutableSet.of(
            ItemID.APPLE_SAPLING,
            ItemID.BANANA_SAPLING,
            ItemID.ORANGE_SAPLING,
            ItemID.CURRY_SAPLING,
            ItemID.PINEAPPLE_SAPLING,
            ItemID.PAPAYA_SAPLING,
            ItemID.PALM_SAPLING,
            ItemID.DRAGONFRUIT_SAPLING
    );

    public static final Set<Integer> HARDWOOD_TREE_SAPLING_IDS = ImmutableSet.of(
            ItemID.TEAK_SAPLING,
            ItemID.MAHOGANY_SAPLING
    );

    public static final Set<Integer> ANIMA_SEED_IDS = ImmutableSet.of(
            ItemID.KRONOS_SEED,
            ItemID.IASOR_SEED,
            ItemID.ATTAS_SEED
    );

    public static final Set<Integer> CACTUS_SEED_IDS = ImmutableSet.of(
            ItemID.CACTUS_SEED,
            ItemID.POTATO_CACTUS_SEED
    );

    public static final Set<Integer> COMPOST_IDS = ImmutableSet.of(
            ItemID.COMPOST,
            ItemID.SUPERCOMPOST,
            ItemID.ULTRACOMPOST,
            ItemID.BOTTOMLESS_COMPOST_BUCKET,
            ItemID.BOTTOMLESS_COMPOST_BUCKET_22997
    );

    public static final Set<Integer> WATERING_CAN_IDS = ImmutableSet.of(
            ItemID.WATERING_CAN1,
            ItemID.WATERING_CAN2,
            ItemID.WATERING_CAN3,
            ItemID.WATERING_CAN4,
            ItemID.WATERING_CAN5,
            ItemID.WATERING_CAN6,
            ItemID.WATERING_CAN7,
            ItemID.WATERING_CAN8
    );

    public static final Set<Integer> GRIMY_HERB_IDS = ImmutableSet.of(
            ItemID.GRIMY_GUAM_LEAF,
            ItemID.GRIMY_MARRENTILL,
            ItemID.GRIMY_TARROMIN,
            ItemID.GRIMY_HARRALANDER,
            ItemID.GRIMY_RANARR_WEED,
            ItemID.GRIMY_TOADFLAX,
            ItemID.GRIMY_IRIT_LEAF,
            ItemID.GRIMY_AVANTOE,
            ItemID.GRIMY_KWUARM,
            ItemID.GRIMY_SNAPDRAGON,
            ItemID.GRIMY_CADANTINE,
            ItemID.GRIMY_LANTADYME,
            ItemID.GRIMY_DWARF_WEED,
            ItemID.GRIMY_TORSTOL
    );

    public static final int TOOL_WIDGET_ID = 125;
    public static final Supplier<Widget> TOOLS_WIDGET = () -> Widgets.get(TOOL_WIDGET_ID, 0);
    public static final Supplier<Widget> TOOLS_CLOSE_WIDGET = () -> Widgets.get(TOOL_WIDGET_ID, 1, 11);

    public static final Supplier<Widget> TOOLS_WITHDRAW_DIBBER_WIDGET = () -> Widgets.get(TOOL_WIDGET_ID, 9);
    public static final Supplier<Widget> TOOLS_WITHDRAW_SPADE_WIDGET = () -> Widgets.get(TOOL_WIDGET_ID, 10);
    public static final Supplier<Widget> TOOLS_WITHDRAW_SECATEURS_WIDGET = () -> Widgets.get(TOOL_WIDGET_ID, 11);
    public static final Supplier<Widget> TOOLS_WITHDRAW_BOTTOMLESS_BUCKET_WIDGET = () -> Widgets.get(TOOL_WIDGET_ID, 15);
    public static final Supplier<Widget> TOOLS_WITHDRAW_COMPOST_WIDGET = () -> Widgets.get(TOOL_WIDGET_ID, 17);
    public static final Supplier<Widget> TOOLS_WITHDRAW_SUPERCOMPOST_WIDGET = () -> Widgets.get(TOOL_WIDGET_ID, 18);
    public static final Supplier<Widget> TOOLS_WITHDRAW_ULTRACOMPOST_WIDGET = () -> Widgets.get(TOOL_WIDGET_ID, 19);
    public static final Supplier<Widget> TOOLS_WITHDRAW_PLANT_CURE_WIDGET = () -> Widgets.get(TOOL_WIDGET_ID, 19); // TODO id

    public static final Supplier<Widget> TOOLS_DEPOSIT_DIBBER_WIDGET = () -> Widgets.get(TOOL_WIDGET_ID + 1, 2);
    public static final Supplier<Widget> TOOLS_DEPOSIT_SPADE_WIDGET = () -> Widgets.get(TOOL_WIDGET_ID + 1, 3);
    public static final Supplier<Widget> TOOLS_DEPOSIT_SECATEURS_WIDGET = () -> Widgets.get(TOOL_WIDGET_ID + 1, 4);
    public static final Supplier<Widget> TOOLS_DEPOSIT_BOTTOMLESS_BUCKET_WIDGET = () -> Widgets.get(TOOL_WIDGET_ID + 1, 8);
    public static final Supplier<Widget> TOOLS_DEPOSIT_BUCKET_WIDGET = () -> Widgets.get(TOOL_WIDGET_ID + 1, 9);

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
            ObjectID.BANK_CHEST_41493,
            ObjectID.BANK_CHEST_43697,
            NullObjectID.NULL_12308
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
