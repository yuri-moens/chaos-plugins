package io.reisub.unethicalite.birdhouse;

import io.reisub.unethicalite.utils.enums.Log;
import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

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
			keyName = "birdhouseHotkey",
			name = "Start hotkey",
			description = "Start a birdhouse rn from a bank.",
			position = 2
	)
	default Keybind birdhouseHotkey() {
		return new Keybind(KeyEvent.VK_F11, InputEvent.CTRL_DOWN_MASK);
	}

	@ConfigItem(
			keyName = "logs",
			name = "Logs",
			description = "Select which logs to use.",
			position = 3
	)
	default Log logs() {
		return Log.YEW;
	}

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
