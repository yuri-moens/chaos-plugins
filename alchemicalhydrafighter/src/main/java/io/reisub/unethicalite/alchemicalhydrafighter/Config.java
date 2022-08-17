package io.reisub.unethicalite.alchemicalhydrafighter;

import io.reisub.unethicalite.utils.enums.TeleportLocation;
import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaosalchemicalhydrafighter")
public interface Config extends net.runelite.client.config.Config {
  @ConfigItem(
      keyName = "equipment",
      name = "Equipment",
      description = "Equipment to use",
      position = 0)
  default String equipment() {
    return "Slayer helmet\nFire cape\nAmulet of torture\nRada's blessing\nZamorakian hasta\n"
        + "Obsidian platebody\nDragon defender\nObsidian platelegs\nBarrows gloves\n"
        + "Boots of stone\nBerserker ring";
  }

  @ConfigItem(
      keyName = "inventory",
      name = "Inventory",
      description = "Inventory setup to use",
      position = 1)
  default String inventory() {
    return "Rune pouch\nSuper combat potion(4):2\nAntidote+(4)\nBracelet of slaughter\n"
        + "Crafting cape\nDragon dagger(p++)\nShark:10";
  }

  @ConfigItem(
      keyName = "teleportLocation",
      name = "Teleport location",
      description = "Choose where to teleport to for banking",
      position = 2)
  default TeleportLocation teleportLocation() {
    return TeleportLocation.CRAFTING_GUILD_CAPE;
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
