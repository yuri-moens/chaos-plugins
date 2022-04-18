package io.reisub.unethicalite.combathelper.prayer;

import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.widgets.WidgetID;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Getter
public enum QuickPrayer {
    NONE(0, 0),
    THICK_SKIN(WidgetID.QuickPrayer.THICK_SKIN_CHILD_ID, 1),
    BURST_OF_STRENGTH(WidgetID.QuickPrayer.BURST_OF_STRENGTH_CHILD_ID, 4),
    CLARITY_OF_THOUGHT(WidgetID.QuickPrayer.CLARITY_OF_THOUGHT_CHILD_ID, 7),
    SHARP_EYE(WidgetID.QuickPrayer.SHARP_EYE_CHILD_ID, 8),
    MYSTIC_WILL(WidgetID.QuickPrayer.MYSTIC_WILL_CHILD_ID, 9),
    ROCK_SKIN(WidgetID.QuickPrayer.ROCK_SKIN_CHILD_ID, 10),
    SUPERHUMAN_STRENGTH(WidgetID.QuickPrayer.SUPERHUMAN_STRENGTH_CHILD_ID, 13),
    IMPROVED_REFLEXES(WidgetID.QuickPrayer.IMPROVED_REFLEXES_CHILD_ID, 16),
    RAPID_RESTORE(WidgetID.QuickPrayer.RAPID_RESTORE_CHILD_ID, 19),
    RAPID_HEAL(WidgetID.QuickPrayer.RAPID_HEAL_CHILD_ID, 22),
    PROTECT_ITEM(WidgetID.QuickPrayer.PROTECT_ITEM_CHILD_ID, 25),
    HAWK_EYE(WidgetID.QuickPrayer.HAWK_EYE_CHILD_ID, 26),
    MYSTIC_LORE(WidgetID.QuickPrayer.MYSTIC_LORE_CHILD_ID, 27),
    STEEL_SKIN(WidgetID.QuickPrayer.STEEL_SKIN_CHILD_ID, 28),
    ULTIMATE_STRENGTH(WidgetID.QuickPrayer.ULTIMATE_STRENGTH_CHILD_ID, 31),
    INCREDIBLE_REFLEXES(WidgetID.QuickPrayer.INCREDIBLE_REFLEXES_CHILD_ID, 34),
    PROTECT_FROM_MAGIC(WidgetID.QuickPrayer.PROTECT_FROM_MAGIC_CHILD_ID, 37),
    PROTECT_FROM_MISSILES(WidgetID.QuickPrayer.PROTECT_FROM_MISSILES_CHILD_ID, 40),
    PROTECT_FROM_MELEE(WidgetID.QuickPrayer.PROTECT_FROM_MELEE_CHILD_ID, 43),
    EAGLE_EYE(WidgetID.QuickPrayer.EAGLE_EYE_CHILD_ID, 44),
    MYSTIC_MIGHT(WidgetID.QuickPrayer.MYSTIC_MIGHT_CHILD_ID, 45),
    RETRIBUTION(WidgetID.QuickPrayer.RETRIBUTION_CHILD_ID, 46),
    REDEMPTION(WidgetID.QuickPrayer.REDEMPTION_CHILD_ID, 49),
    SMITE(WidgetID.QuickPrayer.SMITE_CHILD_ID, 52),
    PRESERVE(WidgetID.QuickPrayer.PRESERVE_CHILD_ID, 55),
    CHIVALRY(WidgetID.QuickPrayer.CHIVALRY_CHILD_ID, 60),
    PIETY(WidgetID.QuickPrayer.PIETY_CHILD_ID, 70),
    RIGOUR(WidgetID.QuickPrayer.RIGOUR_CHILD_ID, 74),
    AUGURY(WidgetID.QuickPrayer.AUGURY_CHILD_ID, 77);

    private final int childId;
    private final int level;

    public static Set<QuickPrayer> getBestMeleeBuff(int level, boolean pietyUnlocked) {
        if (level >= PIETY.level && pietyUnlocked) {
            return ImmutableSet.of(PIETY);
        } else if (level >= CHIVALRY.level && pietyUnlocked) {
            return ImmutableSet.of(CHIVALRY);
        } else {
            Set<QuickPrayer> quickPrayers = new HashSet<>();

            QuickPrayer bestStrength = getBestStrength(level);
            QuickPrayer bestAttack = getBestAttack(level);
            QuickPrayer bestDefence = getBestDefence(level);

            if (bestAttack != null) {
                quickPrayers.add(bestAttack);
            }

            if (bestStrength != null) {
                quickPrayers.add(bestStrength);
            }

            quickPrayers.add(bestDefence);

            return quickPrayers;
        }
    }

    private static QuickPrayer getBestAttack(int level) {
        if (level >= INCREDIBLE_REFLEXES.level) {
            return INCREDIBLE_REFLEXES;
        } else if (level >= IMPROVED_REFLEXES.level) {
            return IMPROVED_REFLEXES;
        } else if (level >= CLARITY_OF_THOUGHT.level) {
            return CLARITY_OF_THOUGHT;
        }

        return null;
    }

    private static QuickPrayer getBestStrength(int level) {
        if (level >= ULTIMATE_STRENGTH.level) {
            return ULTIMATE_STRENGTH;
        } else if (level >= SUPERHUMAN_STRENGTH.level) {
            return SUPERHUMAN_STRENGTH;
        } else if (level >= BURST_OF_STRENGTH.level) {
            return BURST_OF_STRENGTH;
        }

        return null;
    }

    public static QuickPrayer getBestDefence(int level) {
        if (level >= STEEL_SKIN.level) {
            return STEEL_SKIN;
        } else if (level >= ROCK_SKIN.level) {
            return ROCK_SKIN;
        } else {
            return THICK_SKIN;
        }
    }

    public static Set<QuickPrayer> getBestRangedBuff(int level, boolean rigourUnlocked) {
        QuickPrayer bestDefense = getBestDefence(level);

        if (level >= RIGOUR.level && rigourUnlocked) {
            return ImmutableSet.of(RIGOUR);
        } else if (level >= EAGLE_EYE.level) {
            return ImmutableSet.of(EAGLE_EYE, bestDefense);
        } else if (level >= HAWK_EYE.level) {
            return ImmutableSet.of(HAWK_EYE, bestDefense);
        } else if (level >= SHARP_EYE.level) {
            return ImmutableSet.of(SHARP_EYE, bestDefense);
        }

        return ImmutableSet.of(bestDefense);
    }

    public static Set<QuickPrayer> getBestMagicBuff(int level, boolean auguryUnlocked) {
        QuickPrayer bestDefense = getBestDefence(level);

        if (level >= AUGURY.level && auguryUnlocked) {
            return ImmutableSet.of(AUGURY);
        } else if (level >= MYSTIC_MIGHT.level) {
            return ImmutableSet.of(MYSTIC_MIGHT, bestDefense);
        } else if (level >= MYSTIC_LORE.level) {
            return ImmutableSet.of(MYSTIC_LORE, bestDefense);
        } else if (level >= MYSTIC_WILL.level) {
            return ImmutableSet.of(MYSTIC_WILL, bestDefense);
        }

        return ImmutableSet.of(bestDefense);
    }
}
