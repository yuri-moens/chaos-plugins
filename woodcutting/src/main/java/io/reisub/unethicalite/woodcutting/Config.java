package io.reisub.unethicalite.woodcutting;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaoswoodcutting")
public interface Config extends net.runelite.client.config.Config {
  @ConfigItem(
      keyName = "location",
      name = "Location",
      description = "Choose what to chop and where",
      position = 0)
  default Location location() {
    return Location.YEW_WOODCUTTING_GUILD;
  }

  @ConfigItem(
      keyName = "drop",
      name = "Drop",
      description = "Drop logs",
      position = 1)
  default boolean drop() {
    return false;
  }

  @ConfigItem(
      keyName = "birdNests",
      name = "Pick up bird nests",
      description = "Pick up bird nests",
      position = 2)
  default boolean birdNests() {
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
