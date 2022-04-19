package io.reisub.unethicalite.enchanter;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaosenchanter")
public interface Config extends net.runelite.client.config.Config {
  @ConfigItem(
      keyName = "spell",
      name = "Spell",
      description = "Select the enchanting spell",
      position = 0)
  default EnchantSpell spell() {
    return EnchantSpell.LEVEL_1;
  }

  @ConfigItem(keyName = "item", name = "Item", description = "Select the target item", position = 1)
  default EnchantItem item() {
    return EnchantItem.ALL;
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
