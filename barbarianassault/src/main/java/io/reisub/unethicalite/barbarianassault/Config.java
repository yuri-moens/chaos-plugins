package io.reisub.unethicalite.barbarianassault;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;

@ConfigGroup("chaosbarbarianassault")
public interface Config extends net.runelite.client.config.Config {

  @ConfigItem(
      keyName = "useFoodHotkey",
      name = "Use food hotkey",
      description = "Pressing this hotkey will use the correct poisoned food on the nearest"
          + "healer",
      position = 0)
  default Keybind useFoodHotkey() {
    return new Keybind(KeyEvent.VK_V, 0);
  }

  @ConfigItem(
      keyName = "dropHotkey",
      name = "Drop hotkey",
      description = "Pressing this hotkey will drop all (poisoned) food and eggs",
      position = 1)
  default Keybind dropHotkey() {
    return new Keybind(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK);
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
