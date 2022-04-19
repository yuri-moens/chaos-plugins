package io.reisub.unethicalite.agility;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaosagility")
public interface Config extends net.runelite.client.config.Config {
  @ConfigItem(
      position = 0,
      keyName = "courseSelection",
      name = "Select course",
      description = "Select a course to run")
  default Course courseSelection() {
    return Course.SEERS;
  }

  @ConfigItem(
      position = 10,
      keyName = "highAlch",
      name = "High alch",
      description = "Enable to high alch between obstacles")
  default boolean highAlch() {
    return false;
  }

  @ConfigItem(
      position = 11,
      keyName = "alchItems",
      name = "High alch items",
      description = "Names or IDs of items to alch, one item per line")
  default String alchItems() {
    return "";
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
