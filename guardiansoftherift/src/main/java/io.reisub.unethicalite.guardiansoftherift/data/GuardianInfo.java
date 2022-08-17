package io.reisub.unethicalite.guardiansoftherift.data;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import lombok.Value;
import net.runelite.api.ItemID;
import net.runelite.api.Quest;
import net.runelite.api.QuestState;
import net.runelite.api.Skill;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.game.Skills;
import net.unethicalite.client.Static;

@Value
public class GuardianInfo {

  public static final GuardianInfo BLOOD = new GuardianInfo(77, ItemID.BLOOD_RUNE,
      26894, 4364, 43708, RuneType.CATALYTIC, CellType.OVERCHARGED,
      Quest.SINS_OF_THE_FATHER
  );
  public static final GuardianInfo DEATH = new GuardianInfo(65, ItemID.DEATH_RUNE,
      26893, 4363, 43707, RuneType.CATALYTIC, CellType.OVERCHARGED,
      Quest.MOURNINGS_END_PART_II
  );
  public static final GuardianInfo LAW = new GuardianInfo(54, ItemID.LAW_RUNE,
      26898, 4362, 43712, RuneType.CATALYTIC, CellType.STRONG,
      Quest.TROLL_STRONGHOLD
  );
  public static final GuardianInfo NATURE = new GuardianInfo(44, ItemID.NATURE_RUNE,
      26897, 4361, 43711, RuneType.CATALYTIC, CellType.STRONG, null
  );
  public static final GuardianInfo CHAOS = new GuardianInfo(35, ItemID.CHAOS_RUNE,
      26892, 4360, 43706, RuneType.CATALYTIC, CellType.MEDIUM, null
  );
  public static final GuardianInfo COSMIC = new GuardianInfo(27, ItemID.COSMIC_RUNE,
      26896, 4359, 43710, RuneType.CATALYTIC, CellType.MEDIUM, null
  );
  public static final GuardianInfo BODY = new GuardianInfo(20, ItemID.BODY_RUNE,
      26895, 4358, 43709, RuneType.CATALYTIC, CellType.WEAK, null
  );
  public static final GuardianInfo FIRE = new GuardianInfo(14, ItemID.FIRE_RUNE,
      26890, 4357, 43704, RuneType.ELEMENTAL, CellType.OVERCHARGED, null
  );
  public static final GuardianInfo EARTH = new GuardianInfo(9, ItemID.EARTH_RUNE,
      26889, 4356, 43703, RuneType.ELEMENTAL, CellType.STRONG, null
  );
  public static final GuardianInfo WATER = new GuardianInfo(5, ItemID.WATER_RUNE,
      26888, 4355, 43702, RuneType.ELEMENTAL, CellType.MEDIUM, null
  );
  public static final GuardianInfo MIND = new GuardianInfo(2, ItemID.MIND_RUNE,
      26891, 4354, 43705, RuneType.CATALYTIC, CellType.WEAK, null
  );
  public static final GuardianInfo AIR = new GuardianInfo(1, ItemID.AIR_RUNE,
      26887, 4353, 43701, RuneType.ELEMENTAL, CellType.WEAK, null
  );

  public static final Set<GuardianInfo> ALL =
      ImmutableSet.of(AIR, MIND, WATER, EARTH, FIRE, BODY, COSMIC, CHAOS, NATURE, LAW, DEATH,
          BLOOD
      );

  int levelRequired;
  int runeId;
  int talismanId;
  int spriteId;
  int objectId;
  RuneType runeType;
  CellType cellType;
  Quest requiredQuest;

  public static GuardianInfo getForObjectId(int objectId) {
    for (GuardianInfo gi : ALL) {
      if (gi.objectId == objectId) {
        return gi;
      }
    }
    return null;
  }

  public boolean haveRequirements() {
    return Skills.getLevel(Skill.RUNECRAFT) >= levelRequired
        && (requiredQuest == null
        || GameThread.invokeLater(() -> requiredQuest.getState(Static.getClient()))
        == QuestState.FINISHED);
  }
}