package io.reisub.unethicalite.wintertodt;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("chaoswintertodt")
public interface Config extends net.runelite.client.config.Config {

  @ConfigItem(
      keyName = "solo",
      name = "Solo mode",
      description = "Play the game solo",
      position = 0
  )
  default boolean solo() { return false; }

  @ConfigItem(
      keyName = "sideSelection",
      name = "Select side",
      description = "Choose which side you want to play the game at",
      position = 1
  )
  default Side sideSelection() {
    return Side.EAST;
  }

  @Range(min = 1, max = 100)
  @ConfigItem(
      keyName = "sideTimeout",
      name = "Side timeout",
      description = "Seconds to wait before switching side because of an incapacitated pyromancer",
      position = 2
  )
  default int sideTimeout() { return 15; }

  @ConfigItem(
      keyName = "openCrates",
      name = "Open crates",
      description = "Enable to open crates.",
      position = 3
  )
  default boolean openCrates() {
    return true;
  }

  @ConfigItem(
      keyName = "hop",
      name = "Hop",
      description = "Hop to different worlds after finishing game.",
      hide = "solo",
      position = 4
  )
  default boolean hop() {
    return true;
  }

  @Range(min = 40, max = 100)
  @ConfigItem(
      keyName = "hopPercentage",
      name = "Hop percentage",
      description = "Minimum percentage the boss' health should be at before hopping. "
          + "Low values risk not getting enough points. 70 is safe, 60 should work, "
          + "anything lower is very risky.",
      unhide = "hop",
      position = 5
  )
  default int hopPercentage() { return 70; }

  @ConfigItem(
      keyName = "food",
      name = "Food",
      description = "Food to eat",
      position = 6
  )
  default String food() {
    return "Cake";
  }

  @ConfigItem(
      keyName = "foodAmount",
      name = "Food amount",
      description = "Amount of food to take",
      position = 7
  )
  default int foodAmount() {
    return 5;
  }

  @Range(min = 1, max = 99)
  @ConfigItem(
      keyName = "eatHp",
      name = "HP to eat",
      description = "HP to start eating at",
      position = 8
  )
  default int eatHp() {
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
