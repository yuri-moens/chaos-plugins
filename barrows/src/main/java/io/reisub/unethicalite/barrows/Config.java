package io.reisub.unethicalite.barrows;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("chaosbarrows")
public interface Config extends net.runelite.client.config.Config {
	@ConfigItem(
			keyName = "food",
			name = "Food",
			description = "What food to use",
			position = 0
	)
	default String food() {
		return "Bass";
	}

	@ConfigItem(
			keyName = "foodQuantity",
			name = "Food quantity",
			description = "How much food to take on a trip",
			position = 1
	)
	default int foodQuantity() {
		return 5;
	}

	@ConfigItem(
			keyName = "percentage",
			name = "Percentage",
			description = "What reward percentage we should try to get",
			position = 2
	)
	default Percentage percentage() {
		return Percentage.DEATH_RUNES;
	}

	@ConfigItem(
			keyName = "useHousePool",
			name = "Use house pool",
			description = "Use the pool at your house",
			position = 3
	)
	default boolean useHousePool() {
		return false;
	}

	@ConfigItem(
			keyName = "useFriendsHousePool",
			name = "Use friend's house pool",
			description = "Use the pool at your friend's house",
			position = 4
	)
	default boolean useFriendsHousePool() {
		return false;
	}

	@ConfigSection(
			keyName = "brothersConfig",
			name = "Brothers",
			description = "Configure which brothers to kill",
			closedByDefault = true,
			position = 100
	)
	String brothersConfig = "brothersConfig";

	@ConfigItem(
			keyName = "ahrim",
			name = "Ahrim",
			description = "Enable to kill Ahrim",
			section = "brothersConfig",
			position = 101
	)
	default boolean ahrim() {
		return true;
	}

	@ConfigItem(
			keyName = "dharok",
			name = "Dharok",
			description = "Enable to kill Dharok",
			section = "brothersConfig",
			position = 102
	)
	default boolean dharok() {
		return true;
	}

	@ConfigItem(
			keyName = "guthan",
			name = "Guthan",
			description = "Enable to kill Guthan",
			section = "brothersConfig",
			position = 103
	)
	default boolean guthan() {
		return true;
	}

	@ConfigItem(
			keyName = "karil",
			name = "Karil",
			description = "Enable to kill Karil",
			section = "brothersConfig",
			position = 104
	)
	default boolean karil() {
		return true;
	}

	@ConfigItem(
			keyName = "torag",
			name = "Torag",
			description = "Enable to kill Torag",
			section = "brothersConfig",
			position = 105
	)
	default boolean torag() {
		return true;
	}

	@ConfigItem(
			keyName = "verac",
			name = "Verac",
			description = "Enable to kill Verac",
			section = "brothersConfig",
			position = 106
	)
	default boolean verac() {
		return true;
	}

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
