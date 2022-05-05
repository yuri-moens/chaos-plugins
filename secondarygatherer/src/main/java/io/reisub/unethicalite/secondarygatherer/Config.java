package io.reisub.unethicalite.secondarygatherer;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaossecondarygatherer")
public interface Config extends net.runelite.client.config.Config {

  @ConfigItem(
      keyName = "secondary",
      name = "Secondary",
      description = "Select which secondary to gather",
      position = 0)
  default Secondary secondary() {
    return Secondary.WINE_OF_ZAMORAK;
  }

  @ConfigItem(
      keyName = "food",
      name = "Food",
      description = "What food we should use to heal if necessary",
      position = 1)
  default String food() {
    return "Bass";
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
