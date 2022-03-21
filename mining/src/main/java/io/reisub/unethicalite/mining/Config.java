package io.reisub.unethicalite.mining;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaosmining")
public interface Config extends net.runelite.client.config.Config {
	@ConfigItem(
			keyName = "location",
			name = "Location",
			description = "Choose what to mine and where",
			position = 0
	)
	default Location location() { return Location.QUARRY_SANDSTONE; }

	@ConfigItem(
			keyName = "drop",
			name = "Drop",
			description = "Enable to drop ores",
			position = 1
	)
	default boolean drop() { return false; }

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
