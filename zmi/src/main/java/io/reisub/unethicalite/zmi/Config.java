package io.reisub.unethicalite.zmi;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaoszmi")
public interface Config extends net.runelite.client.config.Config {
  @ConfigItem(
      keyName = "useStamina",
      name = "Use stamina potion",
      description = "Use stamina potions",
      position = 0)
  default boolean useStamina() {
    return true;
  }

  @ConfigItem(
      keyName = "usePrayer",
      name = "Use prayer",
      description = "Use prayer for faster healing",
      position = 1)
  default boolean usePrayer() {
    return true;
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
