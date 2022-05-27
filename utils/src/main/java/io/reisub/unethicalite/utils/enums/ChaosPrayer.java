package io.reisub.unethicalite.utils.enums;

import com.google.common.collect.ImmutableSet;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Prayer;
import net.runelite.api.Skill;
import net.runelite.api.Varbits;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.unethicalite.api.game.Skills;
import net.unethicalite.api.game.Vars;

@RequiredArgsConstructor
@Getter
public enum ChaosPrayer {
  THICK_SKIN(Prayer.THICK_SKIN, WidgetID.QuickPrayer.THICK_SKIN_CHILD_ID, 1),
  BURST_OF_STRENGTH(Prayer.BURST_OF_STRENGTH, WidgetID.QuickPrayer.BURST_OF_STRENGTH_CHILD_ID, 4),
  CLARITY_OF_THOUGHT(
      Prayer.CLARITY_OF_THOUGHT, WidgetID.QuickPrayer.CLARITY_OF_THOUGHT_CHILD_ID, 7),
  SHARP_EYE(Prayer.SHARP_EYE, WidgetID.QuickPrayer.SHARP_EYE_CHILD_ID, 8),
  MYSTIC_WILL(Prayer.MYSTIC_WILL, WidgetID.QuickPrayer.MYSTIC_WILL_CHILD_ID, 9),
  ROCK_SKIN(Prayer.ROCK_SKIN, WidgetID.QuickPrayer.ROCK_SKIN_CHILD_ID, 10),
  SUPERHUMAN_STRENGTH(
      Prayer.SUPERHUMAN_STRENGTH, WidgetID.QuickPrayer.SUPERHUMAN_STRENGTH_CHILD_ID, 13),
  IMPROVED_REFLEXES(Prayer.IMPROVED_REFLEXES, WidgetID.QuickPrayer.IMPROVED_REFLEXES_CHILD_ID, 16),
  RAPID_RESTORE(Prayer.RAPID_RESTORE, WidgetID.QuickPrayer.RAPID_RESTORE_CHILD_ID, 19),
  RAPID_HEAL(Prayer.RAPID_HEAL, WidgetID.QuickPrayer.RAPID_HEAL_CHILD_ID, 22),
  PROTECT_ITEM(Prayer.PROTECT_ITEM, WidgetID.QuickPrayer.PROTECT_ITEM_CHILD_ID, 25),
  HAWK_EYE(Prayer.HAWK_EYE, WidgetID.QuickPrayer.HAWK_EYE_CHILD_ID, 26),
  MYSTIC_LORE(Prayer.MYSTIC_LORE, WidgetID.QuickPrayer.MYSTIC_LORE_CHILD_ID, 27),
  STEEL_SKIN(Prayer.STEEL_SKIN, WidgetID.QuickPrayer.STEEL_SKIN_CHILD_ID, 28),
  ULTIMATE_STRENGTH(Prayer.ULTIMATE_STRENGTH, WidgetID.QuickPrayer.ULTIMATE_STRENGTH_CHILD_ID, 31),
  INCREDIBLE_REFLEXES(
      Prayer.INCREDIBLE_REFLEXES, WidgetID.QuickPrayer.INCREDIBLE_REFLEXES_CHILD_ID, 34),
  PROTECT_FROM_MAGIC(
      Prayer.PROTECT_FROM_MAGIC, WidgetID.QuickPrayer.PROTECT_FROM_MAGIC_CHILD_ID, 37),
  PROTECT_FROM_MISSILES(
      Prayer.PROTECT_FROM_MISSILES, WidgetID.QuickPrayer.PROTECT_FROM_MISSILES_CHILD_ID, 40),
  PROTECT_FROM_MELEE(
      Prayer.PROTECT_FROM_MELEE, WidgetID.QuickPrayer.PROTECT_FROM_MELEE_CHILD_ID, 43),
  EAGLE_EYE(Prayer.EAGLE_EYE, WidgetID.QuickPrayer.EAGLE_EYE_CHILD_ID, 44),
  MYSTIC_MIGHT(Prayer.MYSTIC_MIGHT, WidgetID.QuickPrayer.MYSTIC_MIGHT_CHILD_ID, 45),
  RETRIBUTION(Prayer.RETRIBUTION, WidgetID.QuickPrayer.RETRIBUTION_CHILD_ID, 46),
  REDEMPTION(Prayer.REDEMPTION, WidgetID.QuickPrayer.REDEMPTION_CHILD_ID, 49),
  SMITE(Prayer.SMITE, WidgetID.QuickPrayer.SMITE_CHILD_ID, 52),
  PRESERVE(Prayer.PRESERVE, WidgetID.QuickPrayer.PRESERVE_CHILD_ID, 55),
  CHIVALRY(Prayer.CHIVALRY, WidgetID.QuickPrayer.CHIVALRY_CHILD_ID, 60),
  PIETY(Prayer.PIETY, WidgetID.QuickPrayer.PIETY_CHILD_ID, 70),
  RIGOUR(Prayer.RIGOUR, WidgetID.QuickPrayer.RIGOUR_CHILD_ID, 74),
  AUGURY(Prayer.AUGURY, WidgetID.QuickPrayer.AUGURY_CHILD_ID, 77);

  private final Prayer prayer;
  private final int quickPrayerId;
  private final int requiredLevel;

  public int getVarbit() {
    return prayer.getVarbit();
  }

  public double getDrainRate() {
    return prayer.getDrainRate();
  }

  public WidgetInfo getWidgetInfo() {
    return prayer.getWidgetInfo();
  }

  public static Set<ChaosPrayer> getBestMeleeBuff() {
    final int level = Skills.getLevel(Skill.PRAYER);
    final boolean pietyUnlocked = Vars.getBit(Varbits.CAMELOT_TRAINING_ROOM_STATUS) == 8;

    if (level >= PIETY.getRequiredLevel() && pietyUnlocked) {
      return ImmutableSet.of(PIETY);
    } else if (level >= CHIVALRY.getRequiredLevel() && pietyUnlocked) {
      return ImmutableSet.of(CHIVALRY);
    } else {
      Set<ChaosPrayer> quickPrayers = new HashSet<>();

      ChaosPrayer bestStrength = getBestStrength(level);
      ChaosPrayer bestAttack = getBestAttack(level);
      ChaosPrayer bestDefence = getBestDefence(level);

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

  private static ChaosPrayer getBestAttack(int level) {
    if (level >= INCREDIBLE_REFLEXES.getRequiredLevel()) {
      return INCREDIBLE_REFLEXES;
    } else if (level >= IMPROVED_REFLEXES.getRequiredLevel()) {
      return IMPROVED_REFLEXES;
    } else if (level >= CLARITY_OF_THOUGHT.getRequiredLevel()) {
      return CLARITY_OF_THOUGHT;
    }

    return null;
  }

  private static ChaosPrayer getBestStrength(int level) {
    if (level >= ULTIMATE_STRENGTH.getRequiredLevel()) {
      return ULTIMATE_STRENGTH;
    } else if (level >= SUPERHUMAN_STRENGTH.getRequiredLevel()) {
      return SUPERHUMAN_STRENGTH;
    } else if (level >= BURST_OF_STRENGTH.getRequiredLevel()) {
      return BURST_OF_STRENGTH;
    }

    return null;
  }

  private static ChaosPrayer getBestDefence(int level) {
    if (level >= STEEL_SKIN.getRequiredLevel()) {
      return STEEL_SKIN;
    } else if (level >= ROCK_SKIN.getRequiredLevel()) {
      return ROCK_SKIN;
    } else {
      return THICK_SKIN;
    }
  }

  public static Set<ChaosPrayer> getBestRangedBuff() {
    final int level = Skills.getLevel(Skill.PRAYER);
    final boolean rigourUnlocked = Vars.getBit(Varbits.RIGOUR_UNLOCKED) != 0;
    final ChaosPrayer bestDefense = getBestDefence(level);

    if (level >= RIGOUR.getRequiredLevel() && rigourUnlocked) {
      return ImmutableSet.of(RIGOUR);
    } else if (level >= EAGLE_EYE.getRequiredLevel()) {
      return ImmutableSet.of(EAGLE_EYE, bestDefense);
    } else if (level >= HAWK_EYE.getRequiredLevel()) {
      return ImmutableSet.of(HAWK_EYE, bestDefense);
    } else if (level >= SHARP_EYE.getRequiredLevel()) {
      return ImmutableSet.of(SHARP_EYE, bestDefense);
    }

    return ImmutableSet.of(bestDefense);
  }

  public static Set<ChaosPrayer> getBestMagicBuff() {
    final int level = Skills.getLevel(Skill.PRAYER);
    final boolean auguryUnlocked = Vars.getBit(Varbits.AUGURY_UNLOCKED) != 0;
    final ChaosPrayer bestDefense = getBestDefence(level);

    if (level >= AUGURY.getRequiredLevel() && auguryUnlocked) {
      return ImmutableSet.of(AUGURY);
    } else if (level >= MYSTIC_MIGHT.getRequiredLevel()) {
      return ImmutableSet.of(MYSTIC_MIGHT, bestDefense);
    } else if (level >= MYSTIC_LORE.getRequiredLevel()) {
      return ImmutableSet.of(MYSTIC_LORE, bestDefense);
    } else if (level >= MYSTIC_WILL.getRequiredLevel()) {
      return ImmutableSet.of(MYSTIC_WILL, bestDefense);
    }

    return ImmutableSet.of(bestDefense);
  }
}
