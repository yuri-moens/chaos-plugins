package io.reisub.unethicalite.giantsfoundry;

import io.reisub.unethicalite.giantsfoundry.enums.Alloy;
import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaosgiantsfoundry")
public interface Config extends net.runelite.client.config.Config {
  @ConfigItem(keyName = "alloy1", name = "Alloy 1",
      description = "Choose first part of the alloy", position = 0)
  default Alloy alloy1() {
    return Alloy.BRONZE;
  }

  @ConfigItem(keyName = "alloy2", name = "Alloy 2",
      description = "Choose second part of the alloy", position = 1)
  default Alloy alloy2() {
    return Alloy.BRONZE;
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