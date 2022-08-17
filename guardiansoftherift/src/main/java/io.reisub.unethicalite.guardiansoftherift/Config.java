package io.reisub.unethicalite.guardiansoftherift;

import io.reisub.unethicalite.guardiansoftherift.data.RuneType;
import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaosguardiansoftherift")
public interface Config extends net.runelite.client.config.Config {

  @ConfigItem(
      keyName = "fragments",
      name = "Fragments",
      description = "Amount of guardian fragments to mine at the start",
      position = 0)
  default int fragments() {
    return 160;
  }

  @ConfigItem(
      keyName = "focusPoints",
      name = "Focus",
      description = "Focus on one of the types of points",
      position = 1)
  default boolean focusPoints() {
    return false;
  }

  @ConfigItem(
      keyName = "runeTypeFocus",
      name = "Rune type focus",
      description = "Choose what type of rune to focus",
      position = 2)
  default RuneType runeTypeFocus() {
    return RuneType.ELEMENTAL;
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