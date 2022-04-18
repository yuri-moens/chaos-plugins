package io.reisub.unethicalite.combathelper.misc;

import dev.unethicalite.api.commons.Rand;
import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.game.Game;
import dev.unethicalite.api.items.Equipment;
import dev.unethicalite.api.magic.Lunar;
import dev.unethicalite.api.magic.Magic;
import dev.unethicalite.api.magic.Regular;
import dev.unethicalite.api.utils.MessageUtils;
import io.reisub.unethicalite.combathelper.Helper;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Item;
import net.runelite.api.Player;
import net.runelite.api.Varbits;
import net.runelite.api.events.InteractingChanged;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Singleton;
import java.awt.event.KeyEvent;

@Slf4j
@Singleton
public class MiscHelper extends Helper {
    private boolean pkTeleport;

    @Subscribe(priority = 100)
    private void onPlayerSpawned(PlayerSpawned event) {
        if (!config.tpOnDangerousPlayer() || !pkTeleport || Game.getClient().getVar(Varbits.IN_WILDERNESS) == 0 || event.getPlayer() == null) return;

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
        if (!config.tpOnPlayerAttack() || !pkTeleport || Game.getClient().getVar(Varbits.IN_WILDERNESS) == 0) return;

        if (event.getSource() == null || event.getTarget() == null || event.getTarget().getName() == null) return;

        if (event.getSource() instanceof Player && event.getTarget() == Players.getLocal() && Game.getWildyLevel() <= 30) {
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
            plugin.schedule(this::castVengeance, Rand.nextInt(100, 150));
        }
    }

    private void pkTeleport() {
        pkTeleport = false;

        Item amuletOfGlory = Equipment.getFirst((i) -> i.hasAction("Edgeville"));
        if (amuletOfGlory == null) return;

        amuletOfGlory.interact("Edgeville");
        MessageUtils.addMessage("Tried to teleport, disabled PK teleport");
        Time.sleep(1500, 1800);
    }

    private void teleport() {
        if (Regular.TELEPORT_TO_HOUSE.canCast()) {
            Magic.cast(Regular.TELEPORT_TO_HOUSE);
            Time.sleep(1500, 1800);
        }
    }

    private void castVengeance() {
        if (Lunar.VENGEANCE.canCast()) {
            Magic.cast(Lunar.VENGEANCE);
        }
    }
}
