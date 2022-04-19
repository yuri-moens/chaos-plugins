package io.reisub.unethicalite.superglassmake;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaossuperglassmake")
public interface Config extends net.runelite.client.config.Config {
  @ConfigItem(
      keyName = "pickupGlass",
      name = "Pickup glass",
      description = "Pickup any glass spilling out of the inventory.",
      position = 0)
  default boolean pickupGlass() {
    return true;
  }

  @ConfigItem(
      keyName = "useSodaAshFirst",
      name = "Use soda ash first",
      description = "Use all the soda ash before using giant seaweed.",
      position = 1)
  default boolean useSodaAshFirst() {
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
