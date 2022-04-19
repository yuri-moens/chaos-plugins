package io.reisub.unethicalite.nmz;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaosnmz")
public interface Config extends net.runelite.client.config.Config {
  @ConfigItem(
      keyName = "powerSurge",
      name = "Power surge",
      description = "Enable power surge pick up",
      position = 0)
  default boolean powerSurge() {
    return true;
  }

  @ConfigItem(
      keyName = "recurrentDamage",
      name = "Recurrent damage",
      description = "Enable recurrent damage pick up",
      position = 1)
  default boolean recurrentDamage() {
    return true;
  }

  @ConfigItem(
      keyName = "zapper",
      name = "Zapper",
      description = "Enable zapper pick up",
      position = 2)
  default boolean zapper() {
    return true;
  }

  @ConfigItem(
      keyName = "ultimateForce",
      name = "Ultimate force",
      description = "Enable ultimate force pick up",
      position = 3)
  default boolean ultimateForce() {
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
