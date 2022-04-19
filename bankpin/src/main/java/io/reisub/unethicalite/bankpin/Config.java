package io.reisub.unethicalite.bankpin;

import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaosbankpin")
public interface Config extends net.runelite.client.config.Config {
  @ConfigItem(
      keyName = "bankPin",
      name = "PIN",
      description = "Your bank PIN",
      secret = true,
      position = 0)
  default String bankPin() {
    return "1911";
  }
}
