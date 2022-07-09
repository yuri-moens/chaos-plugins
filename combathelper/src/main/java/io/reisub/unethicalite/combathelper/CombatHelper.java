package io.reisub.unethicalite.combathelper;

import com.google.inject.Provides;
import io.reisub.unethicalite.alchemicalhydra.ChaosAlchemicalHydra;
import io.reisub.unethicalite.cerberus.ChaosCerberus;
import io.reisub.unethicalite.combathelper.alch.AlchHelper;
import io.reisub.unethicalite.combathelper.bones.BonesHelper;
import io.reisub.unethicalite.combathelper.boss.BossHelper;
import io.reisub.unethicalite.combathelper.consume.ConsumeHelper;
import io.reisub.unethicalite.combathelper.misc.MiscHelper;
import io.reisub.unethicalite.combathelper.prayer.PrayerHelper;
import io.reisub.unethicalite.combathelper.special.SpecialHelper;
import io.reisub.unethicalite.combathelper.swap.SwapHelper;
import io.reisub.unethicalite.grotesqueguardians.ChaosGrotesqueGuardians;
import io.reisub.unethicalite.zulrah.ChaosZulrah;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.GameState;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.InteractingChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.itemstats.ItemStatPlugin;
import net.runelite.client.plugins.unethicalite.UnethicalitePlugin;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.Game;
import net.unethicalite.client.Static;
import org.pf4j.Extension;

@Singleton
@PluginDescriptor(
    name = "Chaos Combat Helper",
    description = "Various utilities to make combat easier")
@PluginDependency(ItemStatPlugin.class)
@PluginDependency(UnethicalitePlugin.class)
@PluginDependency(ChaosAlchemicalHydra.class)
@PluginDependency(ChaosCerberus.class)
@PluginDependency(ChaosZulrah.class)
@PluginDependency(ChaosGrotesqueGuardians.class)
@Slf4j
@Extension
public class CombatHelper extends Plugin {

  @Inject private Config config;
  @Getter private Actor lastTarget;
  private ScheduledExecutorService executor;
  private List<Helper> helpers;
  @Inject
  @Getter
  private PrayerHelper prayerHelper;
  @Inject
  @Getter
  private SwapHelper swapHelper;
  @Inject
  @Getter
  private MiscHelper miscHelper;

  @Provides
  Config provideConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  protected void startUp() {
    log.info("Starting Chaos Combat Helper");

    executor = Executors.newSingleThreadScheduledExecutor();
    helpers = new ArrayList<>();

    helpers.add(prayerHelper);
    helpers.add(injector.getInstance(ConsumeHelper.class));
    helpers.add(injector.getInstance(SpecialHelper.class));
    helpers.add(injector.getInstance(BonesHelper.class));
    helpers.add(injector.getInstance(AlchHelper.class));
    helpers.add(swapHelper);
    helpers.add(miscHelper);
    helpers.add(injector.getInstance(BossHelper.class));

    for (Helper helper : helpers) {
      helper.startUp();

      Static.getKeyManager().registerKeyListener(helper);
      Static.getEventBus().register(helper);
    }
  }

  @Override
  protected void shutDown() {
    log.info("Stopping Chaos Combat Helper");

    for (Helper helper : helpers) {
      Static.getKeyManager().unregisterKeyListener(helper);
      Static.getEventBus().unregister(helper);

      helper.shutDown();
    }

    helpers.clear();
    executor.shutdownNow();
  }

  public void schedule(Runnable runnable, int delay) {
    executor.schedule(runnable, delay, TimeUnit.MILLISECONDS);
  }

  @Subscribe
  private void onInteractingChanged(InteractingChanged event) {
    if (!isLoggedIn()) {
      return;
    }

    if (event.getSource() == Players.getLocal() && event.getTarget() != null) {
      lastTarget = Players.getLocal().getInteracting();
    }
  }

  @Subscribe
  private void onActorDeath(ActorDeath event) {
    if (!isLoggedIn()) {
      return;
    }

    if (lastTarget == null || lastTarget.isDead()) {
      lastTarget = null;
    }
  }

  public final boolean isLoggedIn() {
    return Game.getState() == GameState.LOGGED_IN;
  }
}
