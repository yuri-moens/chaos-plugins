package io.reisub.unethicalite.mta;

import java.awt.event.KeyEvent;
import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;

@ConfigGroup("chaosmta")
public interface Config extends net.runelite.client.config.Config {
  @ConfigItem(
      keyName = "spellHotkey",
      name = "Spell hotkey",
      description =
          "Use this hotkey to cast telegrab or bones to bananas depending on the minigame",
      position = 0)
  default Keybind spellHotkey() {
    return new Keybind(KeyEvent.VK_Q, 0);
  }

  @ConfigItem(
      keyName = "enableTelegrabHotkey",
      name = "Enable telegrab hotkey",
      description = "Enable the hotkey for telegrabs",
      position = 1)
  default boolean enableTelegrabHotkey() {
    return true;
  }

  @ConfigItem(
      keyName = "enableBonesHotkey",
      name = "Enable B2B hotkey",
      description = "Enable the hotkey for bones to bananas",
      position = 2)
  default boolean enableBonesHotkey() {
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
