package io.reisub.unethicalite.mahoganyhomes;

import io.reisub.unethicalite.mahoganyhomes.data.Home;
import io.reisub.unethicalite.mahoganyhomes.data.Plank;
import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaosmahoganyhomes")
public interface Config extends net.runelite.client.config.Config {
  @ConfigItem(
      keyName = "plank",
      name = "Plank",
      description = "Select the plank type",
      position = 0)
  default Plank plank() {
    return Plank.TEAK;
  }

  @ConfigItem(
      keyName = "startingHome",
      name = "Starting home",
      description = "Select a starting home if you already have a task, otherwise set to none",
      position = 1)
  default Home startingHome() {
    return Home.NONE;
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
