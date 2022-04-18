package io.reisub.unethicalite.combathelper.boss;

import com.openosrs.client.util.WeaponStyle;
import dev.unethicalite.api.game.Combat;
import io.reisub.unethicalite.combathelper.CombatHelper;
import io.reisub.unethicalite.combathelper.Helper;
import io.reisub.unethicalite.combathelper.prayer.QuickPrayer;
import net.runelite.api.Prayer;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.cerberus.CerberusPlugin;
import net.runelite.client.plugins.cerberus.domain.Cerberus;
import net.runelite.client.plugins.cerberus.domain.CerberusAttack;
import net.runelite.client.plugins.zulrah.ZulrahPlugin;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class BossHelper extends Helper {
    @Inject
    private CombatHelper plugin;

    @Inject
    private CerberusPlugin cerberusPlugin;

    @Inject
    private ZulrahPlugin zulrahPlugin;

    @Subscribe
    private void onGameTick(GameTick event) {
        if (config.cerberusPrayerFlick() && cerberusPlugin.getCerberus() != null) {
            cerberusFlick();
        }

        if (config.autoSwapZulrah() && !zulrahPlugin.getZulrahData().isEmpty()) {
            zulrahSwap();
        }

        if (config.zulrahPrayerFlick() && !zulrahPlugin.getZulrahData().isEmpty()) {
            zulrahFlick();
        }
    }

    private void cerberusFlick() {
        List<CerberusAttack> upcomingAttacks = cerberusPlugin.getUpcomingAttacks();
        Prayer prayer = null;

        if (upcomingAttacks != null && !upcomingAttacks.isEmpty()) {
            prayer = upcomingAttacks.get(0).getAttack().getPrayer();
        }

        if (prayer == null) {
            prayer = cerberusPlugin.getPrayer();
        }

        if (prayer != null) {
            switch (prayer) {
                case PROTECT_FROM_MELEE:
                    plugin.getPrayerHelper().setPrayer(QuickPrayer.PROTECT_FROM_MELEE, false);
                    break;
                case PROTECT_FROM_MISSILES:
                    if (cerberusPlugin.getUpcomingAttacks().get(0).getAttack() == Cerberus.Attack.GHOST_RANGED || cerberusPlugin.getCerberus().getLastTripleAttack() != null) {
                        plugin.getPrayerHelper().setPrayer(QuickPrayer.PROTECT_FROM_MISSILES, false);
                    }
                    break;
                case PROTECT_FROM_MAGIC:
                    plugin.getPrayerHelper().setPrayer(QuickPrayer.PROTECT_FROM_MAGIC, false);
                    break;
            }
        }
    }

    private void zulrahSwap() {
        WeaponStyle current = Combat.getCurrentWeaponStyle();

        zulrahPlugin.getZulrahData().forEach(data -> data.getCurrentPhase().ifPresent(phase -> {
            switch (phase.getZulrahNpc().getType()) {
                case MELEE:
                case RANGE:
                    if (current != WeaponStyle.MAGIC) {
                        plugin.getSwapHelper().swap(true, false, WeaponStyle.MAGIC);
                    }
                    break;
                case MAGIC:
                    if (current != WeaponStyle.RANGE) {
                        plugin.getSwapHelper().swap(true, false, WeaponStyle.RANGE);
                    }
                    break;
            }
        }));
    }

    private void zulrahFlick() {
        zulrahPlugin.getZulrahData().forEach(data -> {
            data.getCurrentPhasePrayer().ifPresent(prayer -> {
                switch (prayer) {
                    case PROTECT_FROM_MAGIC:
                        plugin.getPrayerHelper().setPrayer(QuickPrayer.PROTECT_FROM_MAGIC, false);
                        break;
                    case PROTECT_FROM_MISSILES:
                        plugin.getPrayerHelper().setPrayer(QuickPrayer.PROTECT_FROM_MISSILES, false);
                        break;
                }
            });
        });
    }
}
