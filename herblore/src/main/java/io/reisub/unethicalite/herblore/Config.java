package io.reisub.unethicalite.herblore;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaosherblore")

public interface Config extends net.runelite.client.config.Config {
  @ConfigItem(
      keyName = "task",
      name = "Task",
      description = "Choose what to do. Selecting make potions will also clean herbs and make unfinished potions if necessary. Tarring will also clean herbs if necessary.",
      position = 0
  )
  default HerbloreTask task() {
    return HerbloreTask.CLEAN_HERBS;
  }

  @ConfigItem(
      keyName = "quantity",
      name = "Quantity",
      description = "Choose how many materials you want to use. 0 is unlimited and will work until you're out of materials.",
      position = 1
  )
  default int quantity() {
    return 0;
  }

  @ConfigItem(
      keyName = "herb",
      name = "Herb",
      description = "Select which herbs to clean, tar or make unfinished potions with. Not necessary to set when making potions, will automatically choose the right herbs.",
      position = 2
  )
  default Herb herb() {
    return Herb.ALL;
  }

  @ConfigItem(
      keyName = "base",
      name = "Base",
      description = "Select which base to use to make unfinished potion with. Not necessary to set when making potions, will automatically choose the right base.",
      position = 3
  )
  default Base base() {
    return Base.VIAL_OF_WATER;
  }

  @ConfigItem(
      keyName = "potion",
      name = "Potion",
      description = "Select which potions to make.",
      position = 4
  )
  default Potion potion() {
    return Potion.SARADOMIN_BREW;
  }

  @ConfigItem(
      keyName = "secondary",
      name = "Secondary",
      description = "Select which secondary to process.",
      position = 5
  )
  default Secondary secondary() {
    return Secondary.ALL;
  }

  @ConfigItem(
      keyName = "startButton",
      name = "Start/Stop",
      description = "Start the script",
      position = Integer.MAX_VALUE
  )
  default Button startButton() {
    return new Button();
  }
}