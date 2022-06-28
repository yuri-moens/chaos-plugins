package io.reisub.unethicalite.shafter;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaosshafter")
public interface Config extends net.runelite.client.config.Config {
  @ConfigItem(
      keyName = "type",
      name = "Type",
      description = "Wood type",
      position = 0)
  default Type type() {
    return Type.MAPLE;
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
