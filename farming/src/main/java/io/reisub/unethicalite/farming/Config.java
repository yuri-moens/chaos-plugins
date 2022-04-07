package io.reisub.unethicalite.farming;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Keybind;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

@ConfigGroup("chaosfarming")
public interface Config extends net.runelite.client.config.Config {
	@ConfigItem(
			keyName = "seedsMode",
			name = "Seeds mode",
			description = "Choose which seeds to plant, more information in the readme",
			position = 0
	)
	default SeedsMode seedsMode() { return SeedsMode.LOWEST_FIRST; }

	@ConfigItem(
			keyName = "seedsToKeep",
			name = "Seeds to keep",
			description = "Choose which seeds to keep some of in your bank, useful for seeds used in farming contracts",
			position = 1
	)
	default String seedsToKeep() { return "Snapdragon seed\nCadantine seed\nLantadyme seed\nDwarf weed seed\nTorstol seed"; }

	@ConfigItem(
			keyName = "amountToKeep",
			name = "Amount to keep",
			description = "Choose how many seeds to keep of each kind",
			position = 2
	)
	default int amountToKeep() { return 10; }

	@ConfigItem(
			keyName = "useExplorersRing",
			name = "Use Explorer's ring",
			description = "Use the Explorer's ring to teleport to Falador",
			position = 2
	)
	default boolean useExplorersRing() { return false; }

	@ConfigItem(
			keyName = "useArdougneCloak",
			name = "Use Ardougne cloak",
			description = "Use the Ardougne cloak to teleport to Ardougne",
			position = 3
	)
	default boolean useArdougneCloak() { return false; }

	@ConfigItem(
			keyName = "useXericsTalisman",
			name = "Use Xeric's talisman",
			description = "Use Xeric's talisman to teleport to Hosidius",
			position = 4
	)
	default boolean useXericsTalisman() { return false; }

	@ConfigSection(
			keyName = "oneClickConfig",
			name = "One click",
			description = "Configure one click mode",
			position = 200
	)
	String oneClickConfig = "oneClickConfig";

	@ConfigItem(
			keyName = "oneClickMode",
			name = "One click mode",
			description = "Enable one click mode to quickly do farming actions manually",
			section = "oneClickConfig",
			position = 201
	)
	default boolean oneClickMode() { return false; }

	@ConfigItem(
			keyName = "oneClickNote",
			name = "One click note",
			description = "Enable one click mode on any produce that can be noted",
			hidden = true,
			unhide = "oneClickMode",
			section = "oneClickConfig",
			position = 202
	)
	default boolean oneClickNote() { return false; }

	@ConfigItem(
			keyName = "oneClickCompostProduce",
			name = "One click compost produce",
			description = "Choose what produce to put in the compost bin",
			hidden = true,
			unhide = "oneClickMode",
			section = "oneClickConfig",
			position = 203
	)
	default String oneClickCompostProduce() { return "Watermelon\nPineapple"; }

	@ConfigSection(
			keyName = "herbConfig",
			name = "Herbs",
			description = "Configure what herb patches to farm",
			position = 200
	)
	String herbConfig = "herbConfig";

	@ConfigItem(
			keyName = "faladorHerb",
			name = "Falador",
			description = "Enable patch",
			section = "herbConfig",
			position = 201
	)
	default boolean faladorHerb() { return true; }

	@ConfigItem(
			keyName = "portPhasmatysHerb",
			name = "Port Phasmatys",
			description = "Enable patch",
			section = "herbConfig",
			position = 202
	)
	default boolean portPhasmatysHerb() { return true; }

	@ConfigItem(
			keyName = "catherbyHerb",
			name = "Catherby",
			description = "Enable patch",
			section = "herbConfig",
			position = 203
	)
	default boolean catherbyHerb() { return true; }

	@ConfigItem(
			keyName = "ardougneHerb",
			name = "Ardougne",
			description = "Enable patch",
			section = "herbConfig",
			position = 204
	)
	default boolean ardougneHerb() { return true; }

	@ConfigItem(
			keyName = "hosidiusHerb",
			name = "Hosidius",
			description = "Enable patch",
			section = "herbConfig",
			position = 205
	)
	default boolean hosidiusHerb() { return true; }

	@ConfigItem(
			keyName = "guildHerb",
			name = "Farming Guild",
			description = "Enable patch",
			section = "herbConfig",
			position = 206
	)
	default boolean guildHerb() { return false; }

	@ConfigItem(
			keyName = "trollStrongholdHerb",
			name = "Troll Stronghold",
			description = "Enable patch",
			section = "herbConfig",
			position = 207
	)
	default boolean trollStrongholdHerb() { return false; }

	@ConfigItem(
			keyName = "weissHerb",
			name = "Weiss",
			description = "Enable patch",
			section = "herbConfig",
			position = 208
	)
	default boolean weissHerb() { return false; }

	@ConfigItem(
			keyName = "harmonyHerb",
			name = "Harmony Island",
			description = "Enable patch",
			section = "herbConfig",
			position = 209
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
