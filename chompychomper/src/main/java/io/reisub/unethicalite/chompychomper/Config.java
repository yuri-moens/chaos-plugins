package io.reisub.unethicalite.chompychomper;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaoschompychomper")
public interface Config extends net.runelite.client.config.Config {
  @ConfigItem(
      keyName = "pluck",
      name = "Pluck",
      description = "Pluck chompies, only useful when pet hunting",
      position = 0)
  default boolean pluck() {
    return false;
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
