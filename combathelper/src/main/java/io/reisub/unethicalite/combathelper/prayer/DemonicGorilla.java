package io.reisub.unethicalite.combathelper.prayer;

import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Actor;
import net.runelite.api.HeadIcon;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.coords.WorldArea;

class DemonicGorilla {
  static final int MAX_ATTACK_RANGE = 10; // Needs <= 10 tiles to reach target
  static final int ATTACK_RATE = 5; // 5 ticks between each attack
  static final int ATTACKS_PER_SWITCH = 3; // 3 unsuccessful attacks per style switch

  static final int PROJECTILE_MAGIC_SPEED = 8; // Travels 8 tiles per tick
  static final int PROJECTILE_RANGED_SPEED = 6; // Travels 6 tiles per tick
  static final int PROJECTILE_MAGIC_DELAY = 12; // Requires an extra 12 tiles
  static final int PROJECTILE_RANGED_DELAY = 9; // Requires an extra 9 tiles

  static final AttackStyle[] ALL_REGULAR_ATTACK_STYLES = {
    AttackStyle.MELEE, AttackStyle.RANGED, AttackStyle.MAGIC
  };

  @Getter(AccessLevel.PACKAGE)
  private NPC npc;

  @Getter(AccessLevel.PACKAGE)
  @Setter(AccessLevel.PACKAGE)
  private List<AttackStyle> nextPosibleAttackStyles;

  @Getter(AccessLevel.PACKAGE)
  @Setter(AccessLevel.PACKAGE)
  private int attacksUntilSwitch;

  @Getter(AccessLevel.PACKAGE)
  @Setter(AccessLevel.PACKAGE)
  private int nextAttackTick;

  @Getter(AccessLevel.PACKAGE)
  @Setter(AccessLevel.PACKAGE)
  private int lastTickAnimation;

  @Getter(AccessLevel.PACKAGE)
  @Setter(AccessLevel.PACKAGE)
  private WorldArea lastWorldArea;

  @Getter(AccessLevel.PACKAGE)
  @Setter(AccessLevel.PACKAGE)
  private boolean initiatedCombat;

  @Getter(AccessLevel.PACKAGE)
  @Setter(AccessLevel.PACKAGE)
  private Actor lastTickInteracting;

  @Getter(AccessLevel.PACKAGE)
  @Setter(AccessLevel.PACKAGE)
  private boolean takenDamageRecently;

  @Getter(AccessLevel.PACKAGE)
  @Setter(AccessLevel.PACKAGE)
  private int recentProjectileId;

  @Getter(AccessLevel.PACKAGE)
  @Setter(AccessLevel.PACKAGE)
  private boolean changedPrayerThisTick;

  @Getter(AccessLevel.PACKAGE)
  @Setter(AccessLevel.PACKAGE)
  private boolean changedAttackStyleThisTick;

  @Getter(AccessLevel.PACKAGE)
  @Setter(AccessLevel.PACKAGE)
  private boolean changedAttackStyleLastTick;

  @Getter(AccessLevel.PACKAGE)
  @Setter(AccessLevel.PACKAGE)
  private HeadIcon lastTickOverheadIcon;

  @Getter(AccessLevel.PACKAGE)
  @Setter(AccessLevel.PACKAGE)
  private int disabledMeleeMovementForTicks;

  DemonicGorilla(NPC npc) {
    this.npc = npc;
    this.nextPosibleAttackStyles = Arrays.asList(ALL_REGULAR_ATTACK_STYLES);
    this.nextAttackTick = -100;
    this.attacksUntilSwitch = ATTACKS_PER_SWITCH;
    this.recentProjectileId = -1;
  }

  HeadIcon getOverheadIcon() {
    NPCComposition composition = this.npc.getComposition();
    if (composition != null) {
      return composition.getOverheadIcon();
    }
    return null;
  }

  enum AttackStyle {
    MAGIC,
    RANGED,
    MELEE,
    BOULDER
  }
}
