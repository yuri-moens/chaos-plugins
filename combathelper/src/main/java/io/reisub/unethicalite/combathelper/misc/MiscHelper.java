package io.reisub.unethicalite.combathelper.misc;

import io.reisub.unethicalite.combathelper.Helper;
import java.awt.event.KeyEvent;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Item;
import net.runelite.api.Player;
import net.runelite.api.Varbits;
import net.runelite.api.events.InteractingChanged;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.api.commons.Rand;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.game.Combat.AttackStyle;
import net.unethicalite.api.game.Game;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.magic.SpellBook;
import net.unethicalite.api.utils.MessageUtils;
import net.unethicalite.client.Static;

@Slf4j
@Singleton
public class MiscHelper extends Helper {
  private static final int VENGEANCE_COOLDOWN = 50;
  private boolean pkTeleport;
  private int lastVengance;

  @Subscribe(priority = 100)
  private void onPlayerSpawned(PlayerSpawned event) {
    if (!config.tpOnDangerousPlayer()
        || !pkTeleport
        || Game.getClient().getVar(Varbits.IN_WILDERNESS) == 0
        || event.getPlayer() == null) {
      return;
    }

    int wildyLevel = Game.getWildyLevel();
    int minLevel = Players.getLocal().getCombatLevel() - wildyLevel;
    int maxLevel = Players.getLocal().getCombatLevel() + wildyLevel;

    Player enemy = event.getPlayer();
    if (enemy.getSkullIcon() != null
        && enemy.getCombatLevel() <= maxLevel
        && enemy.getCombatLevel() >= minLevel
        && wildyLevel <= 30) {
      log.info("Dangerous player spawned: " + event.getPlayer().getName());
      plugin.schedule(this::pkTeleport, 0);
    }
  }

  @Subscribe(priority = 100)
  private void onInteractingChanged(InteractingChanged event) {
    if (!config.tpOnPlayerAttack()
        || !pkTeleport
        || Game.getClient().getVar(Varbits.IN_WILDERNESS) == 0) {
      return;
    }

    if (event.getSource() == null
        || event.getTarget() == null
        || event.getTarget().getName() == null) {
      return;
    }

    if (event.getSource() instanceof Player
        && event.getTarget() == Players.getLocal()
        && Game.getWildyLevel() <= 30) {
      log.info("Player attacking us: " + event.getSource().getName());
      plugin.schedule(this::pkTeleport, 0);
    }
  }

  public void keyPressed(KeyEvent e) {
    if (config.pkTeleport().matches(e)) {
      pkTeleport = !pkTeleport;

      if (pkTeleport) {
        MessageUtils.addMessage("Enabled PK teleport");
      } else {
        MessageUtils.addMessage("Disabled PK teleport");
      }
      e.consume();
    } else if (config.tpHotkey().matches(e)) {
      plugin.schedule(this::teleport, Rand.nextInt(100, 150));
      e.consume();
    } else if (config.vengeanceHotkey().matches(e)) {
      castVengeance();
    } else if (config.attackStyleOneHotkey().matches(e)) {
      GameThread.invoke(() -> Combat.setAttackStyle(AttackStyle.FIRST));
    } else if (config.attackStyleTwoHotkey().matches(e)) {
      GameThread.invoke(() -> Combat.setAttackStyle(AttackStyle.SECOND));
    } else if (config.attackStyleThreeHotkey().matches(e)) {
      GameThread.invoke(() -> Combat.setAttackStyle(AttackStyle.THIRD));
    } else if (config.attackStyleFourHotkey().matches(e)) {
      GameThread.invoke(() -> Combat.setAttackStyle(AttackStyle.FOURTH));
    }
  }

  private void pkTeleport() {
    pkTeleport = false;

    Item amuletOfGlory = Equipment.getFirst((i) -> i.hasAction("Edgeville"));
    if (amuletOfGlory == null) {
      return;
    }

    amuletOfGlory.interact("Edgeville");
    MessageUtils.addMessage("Tried to teleport, disabled PK teleport");
    Time.sleep(1500, 1800);
  }

  private void teleport() {
    if (SpellBook.Standard.TELEPORT_TO_HOUSE.canCast()) {
      SpellBook.Standard.TELEPORT_TO_HOUSE.cast();
      Time.sleep(1500, 1800);
    }
  }

  public void castVengeance() {
    plugin.schedule(() -> {
      if (Static.getClient().getTickCount() - lastVengance > VENGEANCE_COOLDOWN
          && SpellBook.Lunar.VENGEANCE.canCast()) {
        SpellBook.Lunar.VENGEANCE.cast();
        lastVengance = Static.getClient().getTickCount();
      }
    }, Rand.nextInt(100, 150));
  }
}
