package io.reisub.unethicalite.farming;

import net.runelite.client.config.*;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

@ConfigGroup("chaosfarming")
public interface Config extends net.runelite.client.config.Config {
	@ConfigSection(
			keyName = "herbConfig",
			name = "Herbs",
			description = "Configure what herb patches to farm",
			position = 0
	)
	String herbConfig = "herbConfig";

	@ConfigItem(
			keyName = "faladorHerb",
			name = "Falador",
			description = "Enable patch",
			section = "herbConfig",
			position = 1
	)
	default boolean faladorHerb() { return true; }

	@ConfigItem(
			keyName = "portPhasmatysHerb",
			name = "Port Phasmatys",
			description = "Enable patch",
			section = "herbConfig",
			position = 2
	)
	default boolean portPhasmatysHerb() { return true; }

	@ConfigItem(
			keyName = "catherbyHerb",
			name = "Catherby",
			description = "Enable patch",
			section = "herbConfig",
			position = 3
	)
	default boolean catherbyHerb() { return true; }

	@ConfigItem(
			keyName = "ardougneHerb",
			name = "Ardougne",
			description = "Enable patch",
			section = "herbConfig",
			position = 4
	)
	default boolean ardougneHerb() { return true; }

	@ConfigItem(
			keyName = "hosidiusHerb",
			name = "Hosidius",
			description = "Enable patch",
			section = "herbConfig",
			position = 5
	)
	default boolean hosidiusHerb() { return true; }

	@ConfigItem(
			keyName = "guildHerb",
			name = "Farming Guild",
			description = "Enable patch",
			section = "herbConfig",
			position = 6
	)
	default boolean guildHerb() { return false; }

	@ConfigItem(
			keyName = "trollStrongholdHerb",
			name = "Troll Stronghold",
			description = "Enable patch",
			section = "herbConfig",
			position = 7
	)
	default boolean trollStrongholdHerb() { return false; }

	@ConfigItem(
			keyName = "weissHerb",
			name = "Weiss",
			description = "Enable patch",
			section = "herbConfig",
			position = 8
	)
	default boolean weissHerb() { return false; }

	@ConfigItem(
			keyName = "harmonyHerb",
			name = "Harmony Island",
			description = "Enable patch",
			section = "herbConfig",
			position = 9
	)
	default boolean harmonyHerb() { return false; }

	@ConfigItem(
			keyName = "farmingHotkey",
			name = "Hotkey",
			description = "Choose the hotkey to start a farm run",
			position = Integer.MAX_VALUE - 1
	)
	default Keybind farmingHotkey() { return new Keybind(KeyEvent.VK_F6, InputEvent.CTRL_DOWN_MASK); }

	@ConfigItem(
			keyName = "startButton",
			name = "Start/Stop",
			description = "Start the script",
			position = Integer.MAX_VALUE
	)
	default Button startButton() {
		return new Button();
	}
}
