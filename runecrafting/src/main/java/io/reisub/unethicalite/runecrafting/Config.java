package io.reisub.unethicalite.runecrafting;

import io.reisub.unethicalite.runecrafting.data.BankLocation;
import io.reisub.unethicalite.runecrafting.data.Rune;
import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaosrunecrafting")
public interface Config extends net.runelite.client.config.Config {
  @ConfigItem(
      keyName = "rune",
      name = "Rune",
      description = "Select the rune to craft",
      position = 0)
  default Rune rune() {
    return Rune.TRUE_BLOOD;
  }

  @ConfigItem(
      keyName = "bankLocation",
      name = "Bank",
      description = "Select the bank location for some rune types like true blood runes",
      position = 1)
  default BankLocation bankLocation() {
    return BankLocation.CRAFTING_GUILD;
  }

  @ConfigItem(
      keyName = "useStamina",
      name = "Use stamina",
      description = "Use stamina potions",
      position = 2)
  default boolean useStamina() {
    return false;
  }

  @ConfigItem(
      keyName = "stamineTimeLeft",
      name = "Stamina refresh",
      description = "When to refresh stamina potion in seconds",
      position = 3,
      hidden = true,
      unhide = "useStamina")
  default int stamineTimeLeft() {
    return 20;
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
