package io.reisub.unethicalite.daeyaltessence;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaosdaeyaltessence")
public interface Config extends net.runelite.client.config.Config {
  @ConfigItem(
      keyName = "bankGems",
      name = "Bank gems",
      description = "Bank gems when inventory is full",
      position = 0
  )
  default boolean bankGems() {
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
