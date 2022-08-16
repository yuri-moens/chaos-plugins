package io.reisub.unethicalite.guardiansoftheriftsolo.data;

import com.google.common.collect.ImmutableSet;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import lombok.Value;
import net.runelite.api.Animation;
import net.runelite.api.DynamicObject;
import net.runelite.api.GameObject;
import net.runelite.api.ItemID;
import net.runelite.api.Quest;
import net.runelite.api.QuestState;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.game.Skills;
import net.unethicalite.client.Static;

@Value
public class GuardianInfo {

  public static final GuardianInfo AIR =
      new GuardianInfo(1, ItemID.AIR_RUNE, 26887, 43701,
          RuneType.ELEMENTAL, CellType.WEAK, null);
  public static final GuardianInfo MIND =
      new GuardianInfo(2, ItemID.MIND_RUNE, 26891, 43705,
          RuneType.CATALYTIC, CellType.WEAK, null);
  public static final GuardianInfo WATER =
      new GuardianInfo(
          5, ItemID.WATER_RUNE, 26888, 43702,
          RuneType.ELEMENTAL, CellType.MEDIUM, null);
  public static final GuardianInfo EARTH =
      new GuardianInfo(
          9, ItemID.EARTH_RUNE, 26889, 43703,
          RuneType.ELEMENTAL, CellType.STRONG, null);
  public static final GuardianInfo FIRE =
      new GuardianInfo(
          14, ItemID.FIRE_RUNE, 26890, 43704,
          RuneType.ELEMENTAL, CellType.OVERCHARGED, null);
  public static final GuardianInfo BODY =
      new GuardianInfo(20, ItemID.BODY_RUNE, 26895, 43709,
          RuneType.CATALYTIC, CellType.WEAK, null);
  public static final GuardianInfo COSMIC =
      new GuardianInfo(
          27, ItemID.COSMIC_RUNE, 26896, 43710,
          RuneType.CATALYTIC, CellType.MEDIUM, null);
  public static final GuardianInfo CHAOS =
      new GuardianInfo(
          35, ItemID.CHAOS_RUNE, 26892, 43706,
          RuneType.CATALYTIC, CellType.MEDIUM, null);
  public static final GuardianInfo NATURE =
      new GuardianInfo(
          44, ItemID.NATURE_RUNE, 26897, 43711,
          RuneType.CATALYTIC, CellType.STRONG, null);
  public static final GuardianInfo LAW =
      new GuardianInfo(54, ItemID.LAW_RUNE, 26898, 43712,
          RuneType.CATALYTIC, CellType.STRONG, Quest.TROLL_STRONGHOLD
      );
  public static final GuardianInfo DEATH =
      new GuardianInfo(65, ItemID.DEATH_RUNE, 26893, 43707,
          RuneType.CATALYTIC, CellType.OVERCHARGED, Quest.MOURNINGS_END_PART_II
      );
  public static final GuardianInfo BLOOD =
      new GuardianInfo(77, ItemID.BLOOD_RUNE, 26894, 43708,
          RuneType.CATALYTIC, CellType.OVERCHARGED, Quest.SINS_OF_THE_FATHER
      );

  public static final Set<GuardianInfo> ALL =
      ImmutableSet.of(AIR, MIND, WATER, EARTH, FIRE, BODY, COSMIC, CHAOS, NATURE, LAW, DEATH,
          BLOOD
      );

  private static final int GUARDIAN_ACTIVE_ANIMATION = 9363;

  int levelRequired;
  int runeId;
  int talismanId;
  int objectId;
  RuneType runeType;
  CellType cellType;
  Quest requiredQuest;

  public static GuardianInfo getBest() {
    final Set<GuardianInfo> guardians = new HashSet<>();

    for (GuardianInfo guardian : ALL) {
      if (guardian.isActive() && guardian.haveRequirements()) {
        guardians.add(guardian);
      }
    }

    return guardians.stream()
        .max(Comparator.comparing(GuardianInfo::getCellType)
            .thenComparingInt(GuardianInfo::getLevelRequired))
        .orElseThrow();
  }

  public boolean haveRequirements() {
    return Skills.getLevel(Skill.RUNECRAFT) >= levelRequired
        && (requiredQuest == null || GameThread.invokeLater(
            () -> requiredQuest.getState(Static.getClient())) == QuestState.FINISHED);
  }

  public boolean isActive() {
    final TileObject guardian = TileObjects.getNearest(objectId);

    if (guardian == null) {
      return false;
    }

    final Animation animation = ((DynamicObject) ((GameObject) guardian).getRenderable()).getAnimation();
    return animation != null && animation.getId() == GUARDIAN_ACTIVE_ANIMATION;
  }

  public TileObject getObject() {
    return TileObjects.getNearest(objectId);
  }
}