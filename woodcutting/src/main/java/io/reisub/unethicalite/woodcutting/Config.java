package io.reisub.unethicalite.woodcutting;

import io.reisub.unethicalite.woodcutting.data.Location;
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
      keyName = "onlyPickUpOurs",
      name = "Only pick up our nests",
      description = "Don't pick up other people's nests, useful for ironmen",
      hidden = true,
      unhide = "birdNests",
      position = 3)
  default boolean onlyPickUpOurs() {
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
