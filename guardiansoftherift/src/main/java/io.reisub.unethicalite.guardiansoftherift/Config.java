package io.reisub.unethicalite.guardiansoftherift;

import io.reisub.unethicalite.guardiansoftherift.data.RuneType;
import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaosguardiansoftherift")
public interface Config extends net.runelite.client.config.Config {

  @ConfigItem(
      keyName = "ticksBeforeLeavingRemains",
      name = "Ticks before leaving remains",
      description = "Number of ticks at which to stop mining large remains",
      position = 0)
  default int ticksBeforeLeavingRemains() {
    return 180;
  }

  @ConfigItem(
      keyName = "ticksBeforeLeavingRemainsPortalStart",
      name = "Ticks before leaving remains with portal start",
      description = "Number of ticks at which to stop mining large remains with a portal start",
      position = 1)
  default int ticksBeforeLeavingRemainsPortalStart() {
    return 210;
  }

  @ConfigItem(
      keyName = "betterGuardianWaitTime",
      name = "Better guardian wait time",
      description = "Seconds to wait for a better guardian (strong or overcharged) to open",
      position = 2)
  default int betterGuardianWaitTime() {
    return 0;
  }

  @ConfigItem(
      keyName = "guardianPowerLastRun",
      name = "Guardian power last run",
      description = "Set the guardian power at which we should do a last runecrafting run",
      position = 3)
  default int guardianPowerLastRun() {
    return 95;
  }

  @ConfigItem(
      keyName = "balancePoints",
      name = "Auto balance points",
      description = "Try to balance elemental and catalytic points",
      position = 4)
  default boolean balancePoints() {
    return false;
  }

  @ConfigItem(
      keyName = "focusPoints",
      name = "Focus",
      description = "Focus on one of the types of points",
      position = 5)
  default boolean focusPoints() {
    return false;
  }

  @ConfigItem(
      keyName = "runeTypeFocus",
      name = "Rune type focus",
      description = "Choose what type of rune to focus",
      position = 6)
  default RuneType runeTypeFocus() {
    return RuneType.ELEMENTAL;
  }

  @ConfigItem(
      keyName = "disableMind",
      name = "Disable mind",
      description = "Disable the mind altar",
      position = 7)
  default boolean disableMind() {
    return true;
  }

  @ConfigItem(
      keyName = "disableBody",
      name = "Disable body",
      description = "Disable the body altar",
      position = 8)
  default boolean disableBody() {
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