package io.reisub.unethicalite.combathelper;

import com.google.inject.Provides;
import dev.hoot.api.entities.Players;
import dev.hoot.api.game.Game;
import io.reisub.unethicalite.combathelper.alch.AlchHelper;
import io.reisub.unethicalite.combathelper.bones.BonesHelper;
import io.reisub.unethicalite.combathelper.consume.ConsumeHelper;
import io.reisub.unethicalite.combathelper.misc.MiscHelper;
import io.reisub.unethicalite.combathelper.prayer.PrayerHelper;
import io.reisub.unethicalite.combathelper.swap.SwapHelper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.GameState;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

import javax.inject.Inject;
import java.awt.event.KeyEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@PluginDescriptor(
		name = "Chaos Combat Helper",
		description = "Various utilities to make combat easier",
		enabledByDefault = false
)
@Slf4j
@Extension
public class CombatHelper extends Plugin implements KeyListener {
	@Inject
	private KeyManager keyManager;

	@Inject
	private Config config;

	@Provides
	Config provideConfig(ConfigManager configManager) {
		return configManager.getConfig(Config.class);
	}

	@Getter
	private Actor lastTarget;

	private ScheduledExecutorService executor;

	private PrayerHelper prayerHelper;
	private ConsumeHelper consumeHelper;
	private BonesHelper bonesHelper;
	private AlchHelper alchHelper;
	private SwapHelper swapHelper;
	private MiscHelper miscHelper;

	@Override
	protected void startUp() {
		log.info("Starting Chaos Combat Helper");

		keyManager.registerKeyListener(this);
		executor = Executors.newSingleThreadScheduledExecutor();

		prayerHelper = injector.getInstance(PrayerHelper.class);
		consumeHelper = injector.getInstance(ConsumeHelper.class);
		bonesHelper = injector.getInstance(BonesHelper.class);
		alchHelper = injector.getInstance(AlchHelper.class);
		swapHelper = injector.getInstance(SwapHelper.class);
		miscHelper = injector.getInstance(MiscHelper.class);

		prayerHelper.startUp();
		consumeHelper.startUp();
		bonesHelper.startUp();
		alchHelper.startUp();
		swapHelper.startUp();
	}

	@Override
	protected void shutDown() {
		log.info("Stopping Chaos Prayer Flicking");

		prayerHelper.shutDown();
		bonesHelper.shutDown();

		keyManager.unregisterKeyListener(this);
		executor.shutdownNow();
	}

	public void schedule(Runnable runnable, int delay) {
		executor.schedule(runnable, delay, TimeUnit.MILLISECONDS);
	}

	@Subscribe(priority = 100)
	private void onGameTickHighPriority(GameTick event) {
		if (!isLoggedIn()) return;

		prayerHelper.onGameTick(event);
	}

	@Subscribe
	private void onGameTick(GameTick event) {
		if (!isLoggedIn()) return;

		consumeHelper.onGameTick();
		alchHelper.onGameTick();
		swapHelper.onGameTick();
	}

	@Subscribe(priority = 100)
	private void onInteractingChangedHighPriority(InteractingChanged event) {
		if (!isLoggedIn()) return;

		miscHelper.onInteractingChanged(event);
	}

	@Subscribe
	private void onInteractingChanged(InteractingChanged event) {
		if (!isLoggedIn()) return;

		if (event.getSource() == Players.getLocal() && event.getTarget() != null) {
			lastTarget = Players.getLocal().getInteracting();
			prayerHelper.onInteractingChanged();
		}
	}

	@Subscribe
	private void onHitsplatApplied(HitsplatApplied event) {
		if (!isLoggedIn()) return;

		prayerHelper.onHitsplatApplied(event);
	}

	@Subscribe
	private void onGameStateChanged(GameStateChanged event) {
		prayerHelper.onGameStateChanged(event);
		consumeHelper.onGameStateChanged(event);
	}

	@Subscribe
	private void onConfigChanged(ConfigChanged event) {
		if (event.getGroup().equals("chaoscombathelper")) {
			consumeHelper.onConfigChanged(event);
			bonesHelper.onConfigChanged(event);
			alchHelper.onConfigChanged(event);
			swapHelper.onConfigChanged();
		}
	}

	@Subscribe(priority = 100)
	private void onPlayerSpawnedHighPriority(PlayerSpawned event) {
		if (!isLoggedIn()) return;

		miscHelper.onPlayerSpawned(event);
	}

	@Subscribe
	private void onPlayerSpawned(PlayerSpawned event) {
		if (!isLoggedIn()) return;

		prayerHelper.onPlayerSpawned(event);
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event) {
		if (!isLoggedIn()) return;

		bonesHelper.onItemContainerChanged(event);
	}

	@Subscribe
	private void onPlayerDespawned(PlayerDespawned event) {
		if (!isLoggedIn()) return;

		prayerHelper.onPlayerDespawned(event);
	}

	@Subscribe
	private void onNpcSpawned(NpcSpawned event) {
		if (!isLoggedIn()) return;

		prayerHelper.onNpcSpawned(event);
	}

	@Subscribe
	private void onNpcDespawned(NpcDespawned event) {
		if (!isLoggedIn()) return;

		prayerHelper.onNpcDespawned(event);
	}

	@Subscribe
	private void onAnimationChanged(AnimationChanged event) {
		if (!isLoggedIn()) return;

		prayerHelper.onAnimationChanged(event);
	}

	@Subscribe
	private void onVarbitChanged(VarbitChanged event) {
		if (!isLoggedIn()) return;

		consumeHelper.onVarbitChanged(event);
	}

	@Subscribe
	private void onChatMessage(ChatMessage event) {
		if (!isLoggedIn()) return;

		consumeHelper.onChatMessage(event);
		alchHelper.onChatMessage(event);
	}

	@Subscribe
	private void onStatChanged(StatChanged event) {
		if (!isLoggedIn()) return;

		consumeHelper.onStatChanged(event);
	}

	@Subscribe
	private void onActorDeath(ActorDeath event) {
		if (!isLoggedIn()) return;

		if (lastTarget == null || lastTarget.isDead()) {
			lastTarget = null;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		prayerHelper.keyPressed(e);
		consumeHelper.keyPressed(e);
		alchHelper.keyPressed(e);
		swapHelper.keyPressed(e);
		miscHelper.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	public final boolean isLoggedIn() {
		return Game.getState() == GameState.LOGGED_IN;
	}
}
