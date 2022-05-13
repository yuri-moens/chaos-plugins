package io.reisub.unethicalite.tabmaker;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaostabmaker")
public interface Config extends net.runelite.client.config.Config {
  @ConfigItem(
      keyName = "tab",
      name = "Tab",
      description = "Select what tab to make",
      position = 0)
  default MagicTab tab() {
    return MagicTab.HOUSE_TELEPORT;
  }

  @ConfigItem(
      keyName = "amount",
      name = "Amount",
      description = "How many tabs to make, 0 to keep going until out of clay",
      position = 1)
  default int amount() {
    return 0;
  }

  @ConfigItem(
      keyName = "startButton",
      name = "Start/Stop",
      description = "Start the script",
      position = Integer.MAX_VALUE)
  default Button startButton() {
    return new Button();
  }
}
