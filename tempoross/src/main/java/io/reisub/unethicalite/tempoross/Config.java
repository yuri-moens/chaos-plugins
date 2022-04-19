package io.reisub.unethicalite.tempoross;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaostempoross")
public interface Config extends net.runelite.client.config.Config {
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
