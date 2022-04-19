package io.reisub.unethicalite.utils;

import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("chaosutils")
public interface Config extends net.runelite.client.config.Config {
  @Range(min = 50, max = 500)
  @ConfigItem(
      keyName = "minDelay",
      name = "Minimum delay",
      description = "Minimum delay in ms for actions to run after start of a game tick",
      position = 0)
  default int minDelay() {
    return 250;
  }

  @Range(min = 100, max = 550)
  @ConfigItem(
      keyName = "maxDelay",
      name = "Maximum delay",
      description = "Maximum delay in ms for actions to run after start of a game tick",
      position = 1)
  default int maxDelay() {
    return 300;
  }
}
