package io.reisub.unethicalite.smithing;

import io.reisub.unethicalite.utils.enums.Metal;
import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaossmithing")
public interface Config extends net.runelite.client.config.Config {
	@ConfigItem(
			keyName = "metal",
			name = "Type",
			description = "Choose metal type",
			position = 0
	)
	default Metal metal() { return Metal.MITHRIL; }

	@ConfigItem(
			keyName = "product",
			name = "Product",
			description = "Choose product",
			position = 1
	)
	default Product product() { return Product.PLATEBODY; }

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