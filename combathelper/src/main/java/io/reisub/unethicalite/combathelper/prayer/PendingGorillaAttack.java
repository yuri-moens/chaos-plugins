package io.reisub.unethicalite.combathelper.prayer;

import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.Player;

class PendingGorillaAttack {
  @Getter(AccessLevel.PACKAGE)
  private final DemonicGorilla attacker;

  @Getter(AccessLevel.PACKAGE)
  private final DemonicGorilla.AttackStyle attackStyle;

  @Getter(AccessLevel.PACKAGE)
  private final Player target;

  @Getter(AccessLevel.PACKAGE)
  private final int finishesOnTick;

  PendingGorillaAttack(
      final DemonicGorilla attacker,
      final DemonicGorilla.AttackStyle attackStyle,
      final Player target,
      final int finishesOnTick) {
    this.attacker = attacker;
    this.attackStyle = attackStyle;
    this.target = target;
    this.finishesOnTick = finishesOnTick;
  }
}
