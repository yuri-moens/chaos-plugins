package io.reisub.unethicalite.fighter;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup("chaosfighter")
public interface Config extends net.runelite.client.config.Config {
  @ConfigItem(
      keyName = "insertMenu",
      name = "Enable menu option",
      description = "Insert menu option to start and stop fighting",
      position = 0
  )
  default boolean insertMenu() {
    return true;
  }

  @ConfigSection(
      keyName = "targetConfig",
      name = "Target",
      description = "Fighter target configuration",
      closedByDefault = true,
      position = 100
  )
  String targetConfig = "targetConfig";

  @ConfigItem(
      keyName = "targetNames",
      name = "Target names",
      description = "List of target names. One name per line, case insensitive. You can use * to specify using 'contains' so 'Goblin*' will match Goblins but also Cave goblins, Hobgoblins, ...",
      section = "targetConfig",
      position = 101
  )
  default String targetNames() {
    return "Goblin*";
  }

  @ConfigItem(
      keyName = "exactName",
      name = "Exact name",
      description = "Target has to match the name exactly (still case insensitive). Turning this off will make 'goblin' match to Goblins, Cave goblins, Hobgoblins, Revenant goblins, ...",
      section = "targetConfig",
      position = 102
  )
  default boolean exactName() {
    return true;
  }

  @ConfigItem(
      keyName = "enableSuperiors",
      name = "Superiors",
      description = "Enable to also fight superior variants of the target",
      section = "targetConfig",
      position = 103
  )
  default boolean enableSuperiors() {
    return true;
  }

  @Range(
      min = 1,
      max = 64
  )
  @ConfigItem(
      keyName = "searchRadius",
      name = "Search radius",
      description = "Search radius in tiles to look for a target",
      section = "targetConfig",
      position = 104
  )
  default int searchRadius() {
    return 15;
  }

  @ConfigSection(
      keyName = "lootConfig",
      name = "Loot",
      description = "Loot configuration",
      closedByDefault = true,
      position = 200
  )
  String lootConfig = "lootConfig";

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
