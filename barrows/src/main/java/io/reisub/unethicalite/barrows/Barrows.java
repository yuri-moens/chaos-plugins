package io.reisub.unethicalite.barrows;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import dev.unethicalite.api.game.Vars;
import io.reisub.unethicalite.barrows.tasks.DrinkPool;
import io.reisub.unethicalite.barrows.tasks.DrinkPrayerPotion;
import io.reisub.unethicalite.barrows.tasks.EnterCrypt;
import io.reisub.unethicalite.barrows.tasks.FightBrother;
import io.reisub.unethicalite.barrows.tasks.FightMonster;
import io.reisub.unethicalite.barrows.tasks.GoToBarrows;
import io.reisub.unethicalite.barrows.tasks.HandleBank;
import io.reisub.unethicalite.barrows.tasks.LeaveCrypt;
import io.reisub.unethicalite.barrows.tasks.NavigateTunnel;
import io.reisub.unethicalite.barrows.tasks.OpenChest;
import io.reisub.unethicalite.barrows.tasks.OpenTomb;
import io.reisub.unethicalite.barrows.tasks.SearchChest;
import io.reisub.unethicalite.barrows.tasks.SolvePuzzle;
import io.reisub.unethicalite.barrows.tasks.TeleportToFeroxEnclave;
import io.reisub.unethicalite.combathelper.CombatHelper;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ConfigList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.NpcID;
import net.runelite.api.ObjectID;
import net.runelite.api.Varbits;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Barrows",
    description = "Automated fratricide",
    enabledByDefault = false)
@PluginDependency(Utils.class)
@PluginDependency(CombatHelper.class)
@Slf4j
@Extension
public class Barrows extends TickScript {
  public static final int[] FEROX_ENCLAVE_REGIONS = new int[] {12344, 12600};
  public static final int BARROWS_REGION = 14131;
  public static final int CRYPT_REGION = 14231;
  public static final Set<Integer> STAIRCASE_IDS =
      ImmutableSet.of(
          ObjectID.STAIRCASE_20667,
          ObjectID.STAIRCASE_20668,
          ObjectID.STAIRCASE_20669,
          ObjectID.STAIRCASE_20670,
          ObjectID.STAIRCASE_20671,
          ObjectID.STAIRCASE_20672);
  public static final Set<Integer> SARCOPHAGUS_IDS =
      ImmutableSet.of(
          ObjectID.SARCOPHAGUS_20720,
          ObjectID.SARCOPHAGUS_20721,
          ObjectID.SARCOPHAGUS_20722,
          ObjectID.SARCOPHAGUS_20770,
          ObjectID.SARCOPHAGUS_20771,
          ObjectID.SARCOPHAGUS_20772);
  public static final Set<Integer> BROTHER_IDS =
      ImmutableSet.of(
          NpcID.AHRIM_THE_BLIGHTED,
          NpcID.DHAROK_THE_WRETCHED,
          NpcID.GUTHAN_THE_INFESTED,
          NpcID.KARIL_THE_TAINTED,
          NpcID.TORAG_THE_CORRUPTED,
          NpcID.VERAC_THE_DEFILED);
  private final LinkedList<Brother> killOrder = new LinkedList<>();
  @Inject private Config config;
  @Setter private boolean isNewRun;

  @Getter @Setter private Queue<Room> tunnelPath;
  @Getter private Brother currentBrother;
  @Getter @Setter private int skeletonsKilled;
  @Getter @Setter private int bloodwormsKilled;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  protected void onStart() {
    super.onStart();

    reset();

    addTask(HandleBank.class);
    addTask(DrinkPool.class);
    addTask(GoToBarrows.class);
    addTask(LeaveCrypt.class);
    addTask(EnterCrypt.class);
    addTask(DrinkPrayerPotion.class);
    addTask(FightBrother.class);
    addTask(OpenTomb.class);
    addTask(SolvePuzzle.class);
    addTask(TeleportToFeroxEnclave.class);
    addTask(FightMonster.class);
    addTask(OpenChest.class);
    addTask(SearchChest.class);
    addTask(NavigateTunnel.class);
  }

  @Subscribe(priority = 90)
  private void onGameTick(GameTick event) {
    if (!isRunning()) {
      return;
    }

    if (killOrder.isEmpty()) {
      if (currentBrother != null && currentBrother.isDead()) {
        currentBrother = null;
      }

      if (currentBrother == null) {
        for (Brother brother : Brother.values()) {
          if (brother.isInTunnel() && !brother.isDead()) {
            currentBrother = brother;
            break;
          }
        }
      }
    } else if (currentBrother == null || currentBrother.isDead() || currentBrother.isInTunnel()) {
      if (isNewRun && currentBrother == null) {
        currentBrother = killOrder.poll();
      } else if (!isNewRun && currentBrother != null) {
        currentBrother = killOrder.poll();
      }
    }
  }

  public void reset() {
    for (Brother brother : Brother.values()) {
      brother.setInTunnel(false);
    }

    tunnelPath = null;
    isNewRun = true;
    buildKillOrder();
  }

  public int getPotentialWithLastBrother() {
    int potentional = getRewardPotential();

    for (Brother brother : Brother.values()) {
      if (!brother.isDead()) {
        switch (getCurrentBrother()) {
          case KARIL:
          case AHRIM:
            return getRewardPotential() + 100;
          default:
            return getRewardPotential() + 117;
        }
      }
    }

    return potentional;
  }

  private void buildKillOrder() {
    ConfigList killOrderConfigList = ConfigList.parseList(config.killOrder());

    for (String name : killOrderConfigList.getStrings()) {
      for (Brother brother : Brother.values()) {
        if (brother.getName().equalsIgnoreCase(name)) {
          killOrder.add(brother);
        }
      }
    }
  }

  private int getRewardPotential() {
    int potential = 0;

    for (Brother brother : Brother.values()) {
      potential += Vars.getBit(brother.getKilledVarbit());
    }

    return potential * 2 + Vars.getBit(Varbits.BARROWS_REWARD_POTENTIAL);
  }
}
