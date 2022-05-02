package io.reisub.unethicalite.utils;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.Range;

@ConfigGroup("chaosutils")
public interface Config extends net.runelite.client.config.Config {
  @Range(min = 50, max = 500)
  @ConfigItem(
      keyName = "minDelay",
      name = "Minimum delay",
      description = "Minimum delay in ms for actions to run after start of a game tick",
      position = 0)
  default int minDelay() {
    return 250;
  }

  @Range(min = 100, max = 550)
  @ConfigItem(
      keyName = "maxDelay",
      name = "Maximum delay",
      description = "Maximum delay in ms for actions to run after start of a game tick",
      position = 1)
  default int maxDelay() {
    return 300;
  }

  @ConfigItem(
      keyName = "walkingInterruptHotkey",
      name = "Walking interrupt hotkey",
      description = "Hotkey to interrupt walking with",
      position = 2)
  default Keybind walkingInterruptHotkey() {
    return new Keybind(KeyEvent.VK_F12, InputEvent.CTRL_DOWN_MASK + InputEvent.ALT_DOWN_MASK);
  }
}
