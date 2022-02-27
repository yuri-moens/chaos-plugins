package io.reisub.unethicalite.herblore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.ItemID;

@AllArgsConstructor
@Getter
public enum Potion {
    ATTACK_POTION               (ItemID.VIAL_OF_WATER, Herb.GUAM_LEAF, ItemID.GUAM_POTION_UNF, ItemID.EYE_OF_NEWT),
    ANTIPOISON                  (ItemID.VIAL_OF_WATER, Herb.MARRENTILL, ItemID.MARRENTILL_POTION_UNF, ItemID.UNICORN_HORN_DUST),
    STRENGTH_POTION             (ItemID.VIAL_OF_WATER, Herb.TARROMIN, ItemID.TARROMIN_POTION_UNF, ItemID.LIMPWURT_ROOT),
    GUTHIX_REST_TEA             (ItemID.CUP_OF_HOT_WATER, Herb.HARRALANDER, -1, -1),
    COMPOST_POTION              (ItemID.VIAL_OF_WATER, Herb.HARRALANDER, ItemID.HARRALANDER_POTION_UNF, ItemID.VOLCANIC_ASH),
    RESTORE_POTION              (ItemID.VIAL_OF_WATER, Herb.HARRALANDER, ItemID.HARRALANDER_POTION_UNF, ItemID.RED_SPIDERS_EGGS),
    GUTHIX_BALANCE              (ItemID.RESTORE_POTION4, null, ItemID.GUTHIX_BALANCE_UNF, -1),
    ENERGY_POTION               (ItemID.VIAL_OF_WATER, Herb.HARRALANDER, ItemID.HARRALANDER_POTION_UNF, ItemID.CHOCOLATE_DUST),
    DEFENCE_POTION              (ItemID.VIAL_OF_WATER, Herb.RANARR_WEED, ItemID.RANARR_POTION_UNF, ItemID.WHITE_BERRIES),
    AGILITY_POTION              (ItemID.VIAL_OF_WATER, Herb.TOADFLAX, ItemID.TOADFLAX_POTION_UNF, ItemID.TOADS_LEGS),
    COMBAT_POTION               (ItemID.VIAL_OF_WATER, Herb.HARRALANDER, ItemID.HARRALANDER_POTION_UNF, ItemID.GOAT_HORN_DUST),
    PRAYER_POTION               (ItemID.VIAL_OF_WATER, Herb.RANARR_WEED, ItemID.RANARR_POTION_UNF, ItemID.SNAPE_GRASS),
    SUPER_ATTACK                (ItemID.VIAL_OF_WATER, Herb.IRIT_LEAF, ItemID.IRIT_POTION_UNF, ItemID.EYE_OF_NEWT),
    SUPERANTIPOISON             (ItemID.VIAL_OF_WATER, Herb.IRIT_LEAF, ItemID.IRIT_POTION_UNF, ItemID.UNICORN_HORN_DUST),
    FISHING_POTION              (ItemID.VIAL_OF_WATER, Herb.AVANTOE, ItemID.AVANTOE_POTION_UNF, ItemID.SNAPE_GRASS),
    SUPER_ENERGY                (ItemID.VIAL_OF_WATER, Herb.AVANTOE, ItemID.AVANTOE_POTION_UNF, ItemID.MORT_MYRE_FUNGUS),
    HUNTER_POTION               (ItemID.VIAL_OF_WATER, Herb.AVANTOE, ItemID.AVANTOE_POTION_UNF, ItemID.KEBBIT_TEETH_DUST),
    SUPER_STRENGTH              (ItemID.VIAL_OF_WATER, Herb.KWUARM, ItemID.KWUARM_POTION_UNF, ItemID.LIMPWURT_ROOT),
    WEAPON_POISON               (ItemID.VIAL_OF_WATER, Herb.KWUARM, ItemID.KWUARM_POTION_UNF, ItemID.DRAGON_SCALE_DUST),
    SUPER_RESTORE               (ItemID.VIAL_OF_WATER, Herb.SNAPDRAGON, ItemID.SNAPDRAGON_POTION_UNF, ItemID.RED_SPIDERS_EGGS),
    SANFEW_SERUM                (ItemID.SUPER_RESTORE4, null, ItemID.MIXTURE__STEP_14, -1),
    SUPER_DEFENCE               (ItemID.VIAL_OF_WATER, Herb.CADANTINE, ItemID.CADANTINE_POTION_UNF, ItemID.WHITE_BERRIES),
    ANTIDOTE_PLUS               (ItemID.COCONUT_MILK, Herb.TOADFLAX, ItemID.ANTIDOTE_UNF, ItemID.YEW_ROOTS),
    ANTIFIRE_POTION             (ItemID.VIAL_OF_WATER, Herb.LANTADYME, ItemID.LANTADYME_POTION_UNF, ItemID.DRAGON_SCALE_DUST),
    DIVINE_SUPER_ATTACK_POTION          (ItemID.SUPER_ATTACK4, null, -1, ItemID.CRYSTAL_DUST),
    DIVINE_SUPER_DEFENCE_POTION         (ItemID.SUPER_DEFENCE4, null, -1, ItemID.CRYSTAL_DUST),
    DIVINE_SUPER_STRENGTH_POTION        (ItemID.SUPER_STRENGTH4, null, -1, ItemID.CRYSTAL_DUST),
    RANGING_POTION              (ItemID.VIAL_OF_WATER, Herb.DWARF_WEED, ItemID.DWARF_WEED_POTION_UNF, ItemID.WINE_OF_ZAMORAK),
    DIVINE_RANGING_POTION       (ItemID.RANGING_POTION4, null, -1, ItemID.CRYSTAL_DUST),
    MAGIC_POTION                (ItemID.VIAL_OF_WATER, Herb.LANTADYME, ItemID.LANTADYME_POTION_UNF, ItemID.POTATO_CACTUS),
    STAMINA_POTION              (ItemID.SUPER_ENERGY4, null, -1, ItemID.AMYLASE_CRYSTAL),
    ZAMORAK_BREW                (ItemID.VIAL_OF_WATER, Herb.TORSTOL, ItemID.TORSTOL_POTION_UNF, ItemID.JANGERBERRIES),
    DIVINE_MAGIC_POTION         (ItemID.MAGIC_POTION4, null, -1, ItemID.CRYSTAL_DUST),
    ANTIDOTE_PLUS_PLUS          (ItemID.COCONUT_MILK, Herb.IRIT_LEAF, ItemID.ANTIDOTE_UNF_5951, ItemID.MAGIC_ROOTS),
    BASTION_POTION              (ItemID.VIAL_OF_BLOOD, Herb.CADANTINE, ItemID.CADANTINE_BLOOD_POTION_UNF, ItemID.WINE_OF_ZAMORAK),
    BATTLEMAGE_POTION           (ItemID.VIAL_OF_BLOOD, Herb.CADANTINE, ItemID.CADANTINE_BLOOD_POTION_UNF, ItemID.POTATO_CACTUS),
    SARADOMIN_BREW              (ItemID.VIAL_OF_WATER, Herb.TOADFLAX, ItemID.TOADFLAX_POTION_UNF, ItemID.CRUSHED_NEST),
    EXTENDED_ANTIFIRE           (ItemID.ANTIFIRE_POTION4, null, -1, ItemID.LAVA_SCALE_SHARD),
    ANCIENT_BREW                (ItemID.VIAL_OF_WATER, Herb.DWARF_WEED, ItemID.DWARF_WEED_POTION_UNF, ItemID.NIHIL_DUST),
    DIVINE_BASTION_POTION       (ItemID.BASTION_POTION4, null, -1, ItemID.CRYSTAL_DUST),
    DIVINE_BATTLEMAGE_POTION    (ItemID.BATTLEMAGE_POTION4, null, -1, ItemID.CRYSTAL_DUST),
    ANTI_VENOM                  (ItemID.ANTIDOTE4_5952, null, -1, ItemID.ZULRAHS_SCALES),
    SUPER_COMBAT_POTION         (-1, Herb.TORSTOL, -1, -1),
    SUPER_ANTIFIRE_POTION       (ItemID.ANTIFIRE_POTION4, null, -1, ItemID.CRUSHED_SUPERIOR_DRAGON_BONES),
    ANTI_VENOM_PLUS             (ItemID.ANTIVENOM4, Herb.TORSTOL, -1, -1),
    DIVINE_SUPER_COMBAT_POTION  (ItemID.SUPER_COMBAT_POTION4, null, -1, ItemID.CRYSTAL_DUST),
    EXTENDED_SUPER_ANTIFIRE     (ItemID.SUPER_ANTIFIRE_POTION4, null, -1, ItemID.LAVA_SCALE_SHARD);

    private final int baseId;
    private final Herb herb;
    private final int unfinishedId;
    private final int secondaryId;
}