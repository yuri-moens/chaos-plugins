package io.reisub.unethicalite.blastfurnace;

import io.reisub.unethicalite.utils.enums.Metal;
import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaosblastfurnace")
public interface Config extends net.runelite.client.config.Config {
  @ConfigItem(keyName = "metal", name = "Metal", description = "Choose metal type", position = 0)
  default Metal metal() {
    return Metal.MITHRIL;
  }

  @ConfigItem(
      keyName = "useStamina",
      name = "Use stamina pots",
      description = "Enable to use stamina pots",
      position = 1)
  default boolean useStamina() {
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
