package io.reisub.unethicalite.pickpocket;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaospickpocket")
public interface Config extends net.runelite.client.config.Config {
	@ConfigItem(
			keyName = "target",
			name = "Target",
			description = "Select pickpocket target",
			position = 0
	)
	default Target target() { return Target.MASTER_FARMER; }

	@ConfigItem(
			keyName = "eatHP",
			name = "Eat HP",
			description = "HP to eat or bank at",
			position = 1
	)
	default int eatHP() {
		return 10;
	}

	@ConfigItem(
			keyName = "food",
			name = "Food",
			description = "Choose what food to eat",
			position = 2
	)
	default String food() { return "Lobster"; }

	@ConfigItem(
			keyName = "foodQuantity",
			name = "Quantity",
			description = "Choose how much food to take from bank",
			hidden = true,
			hide = "healAtBank",
			position = 3
	)
	default int foodQuantity() { return 5; }

	@ConfigItem(
			keyName = "healAtBank",
			name = "Heal at bank",
			description = "Heal at bank and don't take food with us when thieving",
			position = 4
	)
	default boolean healAtBank() { return false; }

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
