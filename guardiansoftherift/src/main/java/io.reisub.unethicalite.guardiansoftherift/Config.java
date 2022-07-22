package io.reisub.unethicalite.guardiansoftherift;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaosguardiansoftherift")
public interface Config extends net.runelite.client.config.Config {

  @ConfigItem(
      keyName = "focusPoints",
      name = "Focus",
      description =
          "Focus on one of the types of points",
      position = 1)
  default boolean focusPoints() {
    return false;
  }

  @ConfigItem(
      keyName = "elementalFocus",
      name = "Elemental <-> Catalytic",
      description =
          "Choose focus",
      position = 2)
  default boolean elementalFocus() {
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