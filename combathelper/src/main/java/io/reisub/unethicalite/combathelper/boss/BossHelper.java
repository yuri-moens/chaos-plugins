package io.reisub.unethicalite.combathelper.boss;

import com.openosrs.client.util.WeaponStyle;
import dev.unethicalite.api.game.Combat;
import io.reisub.unethicalite.combathelper.CombatHelper;
import io.reisub.unethicalite.combathelper.Helper;
import io.reisub.unethicalite.utils.enums.ChaosPrayer;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Prayer;
import net.runelite.api.Skill;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ProjectileSpawned;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.cerberus.CerberusPlugin;
import net.runelite.client.plugins.cerberus.domain.Cerberus;
import net.runelite.client.plugins.cerberus.domain.CerberusAttack;
import net.runelite.client.plugins.grotesqueguardians.GrotesqueGuardiansPlugin;
import net.runelite.client.plugins.grotesqueguardians.entity.Dusk;
import net.runelite.client.plugins.zulrah.ZulrahPlugin;

@Singleton
public class BossHelper extends Helper {
  @Inject private CombatHelper plugin;

  @Inject private CerberusPlugin cerberusPlugin;

  @Inject private ZulrahPlugin zulrahPlugin;

  @Inject private GrotesqueGuardiansPlugin grotesqueGuardiansPlugin;

  private static final int ZULRAH_RANGED_ANIMATION = 1044;

  private WeaponStyle currentStyle;

  @Subscribe(priority = 95)
  private void onGameTick(GameTick event) {
    currentStyle = Combat.getCurrentWeaponStyle();

    if (config.cerberusPrayerFlick() && cerberusPlugin.getCerberus() != null) {
      cerberusFlick();
    }

    if (config.autoSwapZulrah() && !zulrahPlugin.getZulrahData().isEmpty()) {
      zulrahSwap();
    }

    if (config.zulrahPrayerFlick() && !zulrahPlugin.getZulrahData().isEmpty()) {
      zulrahFlick();
    }

    if (config.autoSwapGrotesqueGuardians() && grotesqueGuardiansPlugin.isOnRoof()) {
      grotesqueGuardiansSwap();
    }

    if (config.grotesqueGuardiansPrayerFlick() && grotesqueGuardiansPlugin.isOnRoof()) {
      grotesqueGuardiansFlick();
    }
  }

  @Subscribe(priority = 80)
  private void onProjectileSpawned(ProjectileSpawned event) {
    if (config.zulrahAutoVengeance() && event.getProjectile().getId() == ZULRAH_RANGED_ANIMATION) {
      zulrahPlugin
          .getZulrahData()
          .forEach(
              data ->
                  data.getCurrentZulrahNpc()
                      .ifPresent(
                          zulrah -> {
                            if (zulrah.getType().getSkill() == Skill.MAGIC) {
                              plugin.getMiscHelper().castVengeance();
                            }
                          }));
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
          plugin.getPrayerHelper().setPrayer(ChaosPrayer.PROTECT_FROM_MELEE, false);
          break;
        case PROTECT_FROM_MISSILES:
          if (cerberusPlugin.getUpcomingAttacks().get(0).getAttack() == Cerberus.Attack.GHOST_RANGED
              || cerberusPlugin.getCerberus().getLastTripleAttack() != null) {
            plugin.getPrayerHelper().setPrayer(ChaosPrayer.PROTECT_FROM_MISSILES, false);
          }
          break;
        case PROTECT_FROM_MAGIC:
          plugin.getPrayerHelper().setPrayer(ChaosPrayer.PROTECT_FROM_MAGIC, false);
          break;
        default:
      }
    }
  }

  private void zulrahSwap() {
    zulrahPlugin
        .getZulrahData()
        .forEach(
            data ->
                data.getCurrentPhase()
                    .ifPresent(
                        phase -> {
                          if (phase.getZulrahNpc().isJad()) {
                            if (currentStyle != WeaponStyle.MAGIC) {
                              plugin.getSwapHelper().swap(true, false, WeaponStyle.MAGIC);
                            }
                          } else {
                            switch (phase.getZulrahNpc().getType()) {
                              case MELEE:
                              case RANGE:
                                if (currentStyle != WeaponStyle.MAGIC) {
                                  plugin.getSwapHelper().swap(true, false, WeaponStyle.MAGIC);
                                }
                                break;
                              case MAGIC:
                                if (currentStyle != WeaponStyle.RANGE) {
                                  plugin.getSwapHelper().swap(true, false, WeaponStyle.RANGE);
                                }
                                break;
                              default:
                            }
                          }
                        }));
  }

  private void zulrahFlick() {
    AtomicReference<Prayer> prayer = new AtomicReference<>();

    zulrahPlugin
        .getZulrahData()
        .forEach(
            data ->
                data.getCurrentPhasePrayer()
                    .ifPresentOrElse(
                        prayer::set,
                        () -> data.getNextPhase()
                            .ifPresent(
                                phase -> {
                                  prayer.set(phase.getAttributes().getPrayer());
                                })));

    if (prayer.get() == null) {
      return;
    }

    switch (prayer.get()) {
      case PROTECT_FROM_MAGIC:
        plugin.getPrayerHelper().setPrayer(ChaosPrayer.PROTECT_FROM_MAGIC, false);
        break;
      case PROTECT_FROM_MISSILES:
        plugin.getPrayerHelper().setPrayer(ChaosPrayer.PROTECT_FROM_MISSILES, false);
        break;
      default:
        break;
    }
  }

  private void grotesqueGuardiansSwap() {
    final Dusk dusk = grotesqueGuardiansPlugin.getDusk();

    if (dusk == null) {
      return;
    }

    switch (dusk.getPhase()) {
      case PHASE_1:
      case PHASE_3:
        if (currentStyle != WeaponStyle.RANGE) {
          plugin.getSwapHelper().swap(true, false, WeaponStyle.RANGE);
        }
        break;
      case PHASE_2:
      case PHASE_4:
        if (currentStyle != WeaponStyle.MELEE) {
          plugin.getSwapHelper().swap(true, false, WeaponStyle.MELEE);
        }
        break;
      default:
        break;
    }
  }

  private void grotesqueGuardiansFlick() {
    final Dusk dusk = grotesqueGuardiansPlugin.getDusk();

    if (dusk == null) {
      return;
    }

    switch (dusk.getPhase()) {
      case PHASE_1:
      case PHASE_3:
        plugin.getPrayerHelper().setPrayer(ChaosPrayer.PROTECT_FROM_MISSILES, false);
        break;
      case PHASE_2:
        plugin.getPrayerHelper().setPrayer(ChaosPrayer.PROTECT_FROM_MELEE, false);
        break;
      case PHASE_4:
        if (dusk.getLastAttackPrayer() == Prayer.PROTECT_FROM_MELEE) {
          plugin.getPrayerHelper().setPrayer(ChaosPrayer.PROTECT_FROM_MELEE, false);
        } else {
          plugin.getPrayerHelper().setPrayer(ChaosPrayer.PROTECT_FROM_MISSILES, false);
        }
        break;
      default:
        break;
    }
  }
}
