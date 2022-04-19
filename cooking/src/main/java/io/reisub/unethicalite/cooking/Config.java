package io.reisub.unethicalite.cooking;

import net.runelite.api.ItemID;
import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaoscooking")
public interface Config extends net.runelite.client.config.Config {
  @ConfigItem(
      keyName = "foodId",
      name = "Food ID",
      description = "ID of the raw food to cook",
      position = 0)
  default int foodId() {
    return ItemID.RAW_SHARK;
  }

  @ConfigItem(
      keyName = "sonicMode",
      name = "Sonic mode",
      description = "Gotta go fast",
      position = 1)
  default boolean sonicMode() {
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
