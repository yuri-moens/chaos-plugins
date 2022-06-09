package io.reisub.unethicalite.sulliuscep;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaossulliuscep")
public interface Config extends net.runelite.client.config.Config {

  @ConfigItem(
      keyName = "brewsToWithdraw",
      name = "Number of brews",
      description = "Amount of Saradomin brews to withdraw from bank",
      position = Integer.MAX_VALUE)
  default int brewsToWithdraw() {
    return 4;
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
