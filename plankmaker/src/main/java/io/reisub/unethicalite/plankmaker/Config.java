package io.reisub.unethicalite.plankmaker;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaosplankmaker")
public interface Config extends net.runelite.client.config.Config {
  @ConfigItem(
      keyName = "logType",
      name = "Log",
      description = "Select the type of logs to  use",
      position = 0)
  default Log logType() {
    return Log.TEAK;
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
