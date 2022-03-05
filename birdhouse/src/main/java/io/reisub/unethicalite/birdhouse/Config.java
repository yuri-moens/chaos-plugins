package io.reisub.unethicalite.birdhouse;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaosbirdhouse")
public interface Config extends net.runelite.client.config.Config {
	@ConfigItem(
			keyName = "farmSeaweed",
			name = "Farm seaweed",
			description = "Harvest and plant seaweed after a birdhouse run.",
			position = 0
	)
	default boolean farmSeaweed() { return true; }

	@ConfigItem(
			keyName = "pickupSpores",
			name = "Pick up spores",
			description = "Pick up spores when underwater.",
			position = 1
	)
	default boolean pickupSpores() { return true; }

	@ConfigItem(
			keyName = "startButton",
			name = "Force Start/Stop",
			description = "The script should automatically start and stop. Use this button for manual overrides.",
			position = Integer.MAX_VALUE
	)
	default Button startButton() {
		return new Button();
	}
}
