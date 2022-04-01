package io.reisub.unethicalite.utils;

import com.google.inject.Provides;
import dev.hoot.api.entities.Players;
import dev.hoot.api.game.Game;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

import javax.inject.Singleton;

@PluginDescriptor(
		name = "Chaos Utils",
		description = "Utilities for Chaos scripts"
)
@Slf4j
@Singleton
@Extension
public class Utils extends Plugin {
	@Provides
	public Config getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Config.class);
	}

	@Override
	public void startUp() {
		log.info(this.getName() + " started");
	}

	@Override
	protected void shutDown() {
		log.info(this.getName() + " stopped");
	}

	public static boolean isLoggedIn() {
		return Game.getClient() != null && Game.getState() == GameState.LOGGED_IN;
	}

	public static boolean isInRegion(int regionId) {
		Player player = Players.getLocal();

		return player.getWorldLocation() != null
				&& player.getWorldLocation().getRegionID() == regionId;
	}

	public static boolean isInMapRegion(int regionId) {
		for (int id : Game.getClient().getMapRegions()) {
			if (id == regionId) {
				return true;
			}
		}

		return false;
	}
}
