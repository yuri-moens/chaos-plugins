package io.reisub.unethicalite.barrows;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaosbarrows")
public interface Config extends net.runelite.client.config.Config {
  @ConfigItem(keyName = "food", name = "Food", description = "What food to use", position = 0)
  default String food() {
    return "Bass";
  }

  @ConfigItem(
      keyName = "foodQuantity",
      name = "Food quantity",
      description = "How much food to take on a trip",
      position = 1)
  default int foodQuantity() {
    return 5;
  }

  @ConfigItem(
      keyName = "potential",
      name = "Potential",
      description = "What reward potential we should try to get",
      position = 2)
  default Potential potential() {
    return Potential.MAX_DEATH_RUNES;
  }

  @ConfigItem(
      keyName = "useHousePool",
      name = "Use house pool",
      description = "Use the pool at your house",
      position = 3)
  default boolean useHousePool() {
    return false;
  }

  @ConfigItem(
      keyName = "useFriendsHousePool",
      name = "Use friend's house pool",
      description = "Use the pool at your friend's house",
      position = 4)
  default boolean useFriendsHousePool() {
    return false;
  }

  @ConfigItem(
      keyName = "killOrder",
      name = "Kill order",
      description = "Customize your kill order",
      position = 5)
  default String killOrder() {
    return "Dharok\nGuthan\nKaril\nAhrim\nTorag\nVerac";
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
