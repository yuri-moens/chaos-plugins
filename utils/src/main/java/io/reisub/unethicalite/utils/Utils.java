package io.reisub.unethicalite.utils;

import com.google.inject.Provides;
import dev.hoot.api.entities.Players;
import dev.hoot.api.game.Game;
import dev.hoot.bot.managers.Static;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;

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

	public static boolean isInRegion(int... regionIds) {
		Player player = Players.getLocal();

		if (player.getWorldLocation() == null) {
			return false;
		}

		for (int regionId : regionIds) {
			if (player.getWorldLocation().getRegionID() == regionId) {
				return true;
			}
		}

		return false;
	}

	public static boolean isInMapRegion(int... regionIds) {
		for (int id : Static.getClient().getMapRegions()) {
			for (int regionId : regionIds) {
				if (id == regionId) {
					return true;
				}
			}
		}

		return false;
	}

	public static Set<String> parseStringList(String list) {
		Set<String> strings = new HashSet<>();

		for (String string : list.split("[\\n;,]")) {
			string = string.split("//")[0].trim();
			strings.add(string);
		}

		return strings;
	}

	public static Set<Integer> parseIntegerList(String list) {
		Set<Integer> integers = new HashSet<>();

		parseStringList(list).forEach(s -> integers.add(Integer.parseInt(s)));

		return integers;
	}

	public static void parseStringOrIntegerList(String list, Set<Integer> integers, Set<String> strings) {
		for (String s : parseStringList(list)) {
			try {
				integers.add(Integer.parseInt(s));
			} catch (NumberFormatException e) {
				strings.add(s);
			}
		}
	}
}
