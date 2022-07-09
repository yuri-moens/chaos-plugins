package io.reisub.unethicalite.combathelper;

import io.reisub.unethicalite.combathelper.bones.Ashes;
import io.reisub.unethicalite.combathelper.bones.Bones;
import io.reisub.unethicalite.combathelper.special.SpecialActivation;
import io.reisub.unethicalite.utils.enums.OverheadPrayer;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Keybind;

@ConfigGroup("chaoscombathelper")
public interface Config extends net.runelite.client.config.Config {
  @ConfigSection(
      keyName = "eatConfig",
      name = "Eating Configuration",
      description = "Configure when to eat",
      position = 0)
  String eatConfig = "eatConfig";

  @ConfigSection(
      keyName = "potsConfig",
      name = "Potions Configuration",
      description = "Configure when to pot",
      closedByDefault = true,
      position = 99)
  String potsConfig = "potsConfig";

  @ConfigSection(
      keyName = "specialConfig",
      name = "Weapon Special Configuration",
      description = "Configure weapon specials",
      closedByDefault = true,
      position = 200)
  String specialConfig = "specialConfig";

  @ConfigSection(
      keyName = "prayerConfig",
      name = "Prayer Configuration",
      description = "Configure prayer flicking and swapping",
      closedByDefault = true,
      position = 300)
  String prayerConfig = "prayerConfig";

  @ConfigSection(
      keyName = "bonesConfig",
      name = "Bones/ashes Configuration",
      description = "Configure bones burying and ashes scattering",
      closedByDefault = true,
      position = 400)
  String bonesConfig = "bonesConfig";

  @ConfigSection(
      keyName = "alchConfig",
      name = "Alch Configuration",
      description = "Configure automatic alching",
      closedByDefault = true,
      position = 500)
  String alchConfig = "alchConfig";

  @ConfigSection(
      keyName = "swapConfig",
      name = "Swap Configuration",
      description = "Configure automatic swapping",
      closedByDefault = true,
      position = 600)
  String swapConfig = "swapConfig";

  @ConfigSection(
      keyName = "miscConfig",
      name = "Misc Configuration",
      description = "Configure miscellaneous features",
      section = "swapConfig",
      closedByDefault = true,
      position = 700)
  String miscConfig = "miscConfig";

  @ConfigItem(
      keyName = "enableEating",
      name = "Enable eating",
      description = "Enable automatic eating based on the minimum and maximum values set below.",
      section = "eatConfig",
      position = 1)
  default boolean enableEating() {
    return true;
  }

  @ConfigItem(
      keyName = "minEatHp",
      name = "Minimum Eat HP",
      description = "Minimum HP to eat at.",
      section = "eatConfig",
      position = 2)
  default int minEatHp() {
    return 10;
  }

  @ConfigItem(
      keyName = "maxEatHp",
      name = "Maximum Eat HP",
      description = "Highest HP to potentially eat at.",
      section = "eatConfig",
      position = 3)
  default int maxEatHp() {
    return 20;
  }

  @ConfigItem(
      keyName = "comboKarambwan",
      name = "Combo karambwan",
      description = "Combo karambwan with other food.",
      section = "eatConfig",
      position = 4)
  default boolean comboKarambwan() {
    return true;
  }

  @ConfigItem(
      keyName = "comboBrew",
      name = "Combo brew",
      description = "Combo Saradomin brew with other food.",
      section = "eatConfig",
      position = 5)
  default boolean comboBrew() {
    return true;
  }

  @ConfigItem(
      keyName = "restoreAfterBrew",
      name = "Restore after brew",
      description = "Drink restore potion after a Saradomin brew.",
      section = "eatConfig",
      hidden = true,
      unhide = "comboBrew",
      position = 6)
  default boolean restoreAfterBrew() {
    return true;
  }

  @ConfigItem(
      keyName = "eatWarnings",
      name = "Warnings",
      description = "Enable warning messages in chat if you don't have any food.",
      section = "eatConfig",
      hidden = true,
      unhide = "enableEating",
      position = 7)
  default boolean eatWarnings() {
    return true;
  }

  @ConfigItem(
      keyName = "drinkPotions",
      name = "Drink potions",
      description = "Enable automatic drinking of potions.",
      section = "potsConfig",
      position = 100)
  default boolean drinkPotions() {
    return true;
  }

  @ConfigItem(
      keyName = "drinkAntiPoison",
      name = "Drink anti-poison",
      description = "Drink an anti-poison potion when poisoned.",
      section = "potsConfig",
      position = 101)
  default boolean drinkAntiPoison() {
    return true;
  }

  @ConfigItem(
      keyName = "onlyCureVenom",
      name = "Only cure venom",
      description =
          "Drink an anti-poison or anti-venom potion when envenomed but not when poisoned.",
      section = "potsConfig",
      position = 102)
  default boolean onlyCureVenom() {
    return false;
  }

  @ConfigItem(
      keyName = "antiPoisonWarnings",
      name = "Warnings",
      description = "Enable warning messages in chat if you don't have any anti-poison.",
      section = "potsConfig",
      hidden = true,
      unhide = "drinkAntiPoison",
      position = 103)
  default boolean antiPoisonWarnings() {
    return true;
  }

  @ConfigItem(
      keyName = "drinkAntiFire",
      name = "Drink anti-fire",
      description =
          "Drink an anti-fire potion when not under its effect or its effect is running out.",
      section = "potsConfig",
      position = 104)
  default boolean drinkAntiFire() {
    return false;
  }

  @ConfigItem(
      keyName = "antiFireWarnings",
      name = "Warnings",
      description = "Enable warning messages in chat if you don't have any anti-fire potions.",
      section = "potsConfig",
      hidden = true,
      unhide = "drinkAntiFire",
      position = 105)
  default boolean antiFireWarnings() {
    return true;
  }

  @ConfigItem(
      keyName = "drinkPrayer",
      name = "Drink prayer potions",
      description = "Drink a prayer potion when prayer points fall below a threshold.",
      section = "potsConfig",
      position = 106)
  default boolean drinkPrayer() {
    return false;
  }

  @ConfigItem(
      keyName = "minPrayerPoints",
      name = "Minimum prayer points",
      description = "Minimum prayer points to drink at.",
      section = "potsConfig",
      hidden = true,
      unhide = "drinkPrayer",
      position = 107)
  default int minPrayerPoints() {
    return 10;
  }

  @ConfigItem(
      keyName = "maxPrayerPoints",
      name = "Maximum prayer points",
      description = "Highest prayer points to potentially drink at",
      section = "potsConfig",
      hidden = true,
      unhide = "drinkPrayer",
      position = 108)
  default int maxPrayerPoints() {
    return 20;
  }

  @ConfigItem(
      keyName = "prayerWarnings",
      name = "Warnings",
      description = "Enable warning messages in chat if you don't have any prayer potions.",
      section = "potsConfig",
      hidden = true,
      unhide = "drinkPrayer",
      position = 109)
  default boolean prayerWarnings() {
    return true;
  }

  @ConfigItem(
      keyName = "drinkStrength",
      name = "Drink strength pots",
      description = "Enable to drink pots to boost strength.",
      section = "potsConfig",
      position = 110)
  default boolean drinkStrength() {
    return false;
  }

  @ConfigItem(
      keyName = "strengthLevel",
      name = "Strength level",
      description = "Drink strength boosting pot below this level.",
      section = "potsConfig",
      hidden = true,
      unhide = "drinkStrength",
      position = 111)
  default int strengthLevel() {
    return 1;
  }

  @ConfigItem(
      keyName = "strengthWarnings",
      name = "Warnings",
      description = "Enable warning messages in chat if you don't have any strength potions.",
      section = "potsConfig",
      hidden = true,
      unhide = "drinkStrength",
      position = 112)
  default boolean strengthWarnings() {
    return true;
  }

  @ConfigItem(
      keyName = "drinkAttack",
      name = "Drink attack pots",
      description = "Enable to drink pots to boost attack.",
      section = "potsConfig",
      position = 113)
  default boolean drinkAttack() {
    return false;
  }

  @ConfigItem(
      keyName = "attackLevel",
      name = "Attack level",
      description = "Drink attack boosting pot below this level.",
      section = "potsConfig",
      hidden = true,
      unhide = "drinkAttack",
      position = 114)
  default int attackLevel() {
    return 1;
  }

  @ConfigItem(
      keyName = "attackWarnings",
      name = "Warnings",
      description = "Enable warning messages in chat if you don't have any attack potions.",
      section = "potsConfig",
      hidden = true,
      unhide = "drinkAttack",
      position = 115)
  default boolean attackWarnings() {
    return true;
  }

  @ConfigItem(
      keyName = "drinkDefence",
      name = "Drink defence pots",
      description = "Enable to drink pots to boost defence.",
      section = "potsConfig",
      position = 116)
  default boolean drinkDefence() {
    return false;
  }

  @ConfigItem(
      keyName = "defenceLevel",
      name = "Defence level",
      description = "Drink defence boosting pot below this level.",
      section = "potsConfig",
      hidden = true,
      unhide = "drinkDefence",
      position = 117)
  default int defenceLevel() {
    return 1;
  }

  @ConfigItem(
      keyName = "defenceWarnings",
      name = "Warnings",
      description = "Enable warning messages in chat if you don't have any defence potions.",
      section = "potsConfig",
      hidden = true,
      unhide = "drinkDefence",
      position = 118)
  default boolean defenceWarnings() {
    return true;
  }

  @ConfigItem(
      keyName = "drinkRanged",
      name = "Drink ranged pots",
      description = "Enable to drink pots to boost ranged.",
      section = "potsConfig",
      position = 119)
  default boolean drinkRanged() {
    return false;
  }

  @ConfigItem(
      keyName = "rangedLevel",
      name = "Ranged level",
      description = "Drink ranged boosting pot below this level.",
      section = "potsConfig",
      hidden = true,
      unhide = "drinkRanged",
      position = 120)
  default int rangedLevel() {
    return 1;
  }

  @ConfigItem(
      keyName = "rangedWarnings",
      name = "Warnings",
      description = "Enable warning messages in chat if you don't have any ranged potions.",
      section = "potsConfig",
      hidden = true,
      unhide = "drinkRanged",
      position = 121)
  default boolean rangedWarnings() {
    return true;
  }

  @ConfigItem(
      keyName = "drinkMagic",
      name = "Drink magic pots",
      description = "Enable to drink pots to boost magic.",
      section = "potsConfig",
      position = 122)
  default boolean drinkMagic() {
    return false;
  }

  @ConfigItem(
      keyName = "magicLevel",
      name = "Magic level",
      description = "Drink magic boosting pot below this level.",
      section = "potsConfig",
      hidden = true,
      unhide = "drinkMagic",
      position = 123)
  default int magicLevel() {
    return 1;
  }

  @ConfigItem(
      keyName = "magicWarnings",
      name = "Warnings",
      description = "Enable warning messages in chat if you don't have any magic potions.",
      section = "potsConfig",
      hidden = true,
      unhide = "drinkMagic",
      position = 124)
  default boolean magicWarnings() {
    return true;
  }

  @ConfigItem(
      keyName = "drinkEnergy",
      name = "Drink energy pots",
      description = "Enable to drink pots to recover energy.",
      section = "potsConfig",
      position = 125)
  default boolean drinkEnergy() {
    return false;
  }

  @ConfigItem(
      keyName = "energyLevel",
      name = "Energy level",
      description = "Drink energy pot below this energy level.",
      section = "potsConfig",
      hidden = true,
      unhide = "drinkEnergy",
      position = 126)
  default int energyLevel() {
    return 10;
  }

  @ConfigItem(
      keyName = "energyWarnings",
      name = "Warnings",
      description = "Enable warning messages in chat if you don't have any energy potions.",
      section = "potsConfig",
      hidden = true,
      unhide = "drinkEnergy",
      position = 127)
  default boolean energyWarnings() {
    return true;
  }

  @ConfigItem(
      keyName = "useSpecial",
      name = "Use special",
      description = "Use weapon special",
      section = "specialConfig",
      position = 201)
  default boolean useSpecial() {
    return false;
  }

  @ConfigItem(
      keyName = "activationMethod",
      name = "Activation method",
      description = "Choose the activation method.",
      section = "specialConfig",
      hidden = true,
      unhide = "useSpecial",
      position = 202)
  default SpecialActivation activationMethod() {
    return SpecialActivation.AUTOMATIC;
  }

  @ConfigItem(
      keyName = "specialHotkey",
      name = "Special hotkey",
      description = "Choose the special hotkey.",
      section = "specialConfig",
      hidden = true,
      unhide = "useSpecial",
      position = 203)
  default Keybind specialHotkey() {
    return new Keybind(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);
  }

  @ConfigItem(
      keyName = "specialCost",
      name = "Special cost",
      description = "Cost of the special attack.",
      section = "specialConfig",
      hidden = true,
      unhide = "useSpecial",
      position = 204)
  default int specialCost() {
    return 100;
  }

  @ConfigItem(
      keyName = "specialWeapon",
      name = "Weapon",
      description =
          "What weapon to use the special of. Leaving this empty will use the currently equipped "
          + "weapon.",
      section = "specialConfig",
      hidden = true,
      unhide = "useSpecial",
      position = 205)
  default String specialWeapon() {
    return "";
  }

  @ConfigItem(
      keyName = "swapFromWeapon",
      name = "Swap from weapon",
      description =
          "Specify a weapon to swap from. Leaving this empty will allow swapping from any weapon.",
      section = "specialConfig",
      hidden = true,
      unhide = "useSpecial",
      position = 206)
  default String swapFromWeapon() {
    return "";
  }

  @ConfigItem(
      keyName = "prayerFlickHotkey",
      name = "Flick hotkey",
      description = "When you press this key prayer flicking will start",
      section = "prayerConfig",
      position = 301)
  default Keybind prayerFlickHotkey() {
    return new Keybind(KeyEvent.VK_BACK_QUOTE, 0);
  }

  @ConfigItem(
      keyName = "deactivateAfterStopping",
      name = "Deactivate prayers after stopping",
      description = "Deactivate prayers after stopping prayer flicking.",
      section = "prayerConfig",
      position = 302)
  default boolean deactivateAfterStopping() {
    return true;
  }

  @ConfigItem(
      keyName = "hotkeyMelee",
      name = "Melee hotkey",
      description = "When you press this key protect from melee will be set as quickprayer",
      section = "prayerConfig",
      position = 303)
  default Keybind hotkeyMelee() {
    return new Keybind(KeyEvent.VK_1, InputEvent.CTRL_DOWN_MASK);
  }

  @ConfigItem(
      keyName = "hotkeyMissiles",
      name = "Missiles hotkey",
      description = "When you press this key protect from missiles will be set as quickprayer",
      section = "prayerConfig",
      position = 304)
  default Keybind hotkeyMissiles() {
    return new Keybind(KeyEvent.VK_2, InputEvent.CTRL_DOWN_MASK);
  }

  @ConfigItem(
      keyName = "hotkeyMagic",
      name = "Magic hotkey",
      description = "When you press this key protect from magic will be set as quickprayer",
      section = "prayerConfig",
      position = 305)
  default Keybind hotkeyMagic() {
    return new Keybind(KeyEvent.VK_3, InputEvent.CTRL_DOWN_MASK);
  }

  @ConfigItem(
      keyName = "hotkeyMeleeBuff",
      name = "Melee buff hotkey",
      description = "When you press this key melee buff(s) will be set as quickprayer",
      section = "prayerConfig",
      position = 306)
  default Keybind hotkeyMeleeBuff() {
    return new Keybind(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK);
  }

  @ConfigItem(
      keyName = "hotkeyRangedBuff",
      name = "Ranged buff hotkey",
      description = "When you press this key ranged buff will be set as quickprayer",
      section = "prayerConfig",
      position = 307)
  default Keybind hotkeyRangedBuff() {
    return new Keybind(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK);
  }

  @ConfigItem(
      keyName = "hotkeyMagicBuff",
      name = "Magic buff hotkey",
      description = "When you press this key magic buff will be set as quickprayer",
      section = "prayerConfig",
      position = 308)
  default Keybind hotkeyMagicBuff() {
    return new Keybind(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK);
  }

  @ConfigItem(
      keyName = "openInventory",
      name = "Open inventory",
      description = "Open inventory after swapping quickprayers",
      section = "prayerConfig",
      position = 309)
  default boolean openInventory() {
    return true;
  }

  @ConfigItem(
      keyName = "allowToggleOff",
      name = "Allow toggling off",
      description =
          "Will allow turning the protect prayer off when pressing the hotkey for the current one.",
      section = "prayerConfig",
      position = 310)
  default boolean allowToggleOff() {
    return true;
  }

  @ConfigItem(
      keyName = "jadPrayerFlick",
      name = "Jad Auto Prayer Flick",
      description = "Automatically swap prayers against Jad.",
      section = "prayerConfig",
      position = 311)
  default boolean jadPrayerFlick() {
    return true;
  }

  @ConfigItem(
      keyName = "demonicGorillaFlick",
      name = "Demonic Gorilla Auto Prayer Flick",
      description = "Automatically swap prayers against Demonic gorillas.",
      section = "prayerConfig",
      position = 313)
  default boolean demonicGorillaFlick() {
    return true;
  }

  @ConfigItem(
      keyName = "zulrahPrayerFlick",
      name = "Zulrah Auto Prayer Flick",
      description = "Automatically swap prayers against Zulrah",
      section = "prayerConfig",
      position = 314)
  default boolean zulrahPrayerFlick() {
    return true;
  }

  @ConfigItem(
      keyName = "cerberusPrayerFlick",
      name = "Cerberus Auto Prayer Flick",
      description = "Automatically swap prayers against Cerberus",
      section = "prayerConfig",
      position = 315)
  default boolean cerberusPrayerFlick() {
    return true;
  }

  @ConfigItem(
      keyName = "grotesqueGuardiansPrayerFlick",
      name = "Grotesque Guardians Auto Prayer Flick",
      description = "Automatically swap prayers against Grotesque Guardians",
      section = "prayerConfig",
      position = 316)
  default boolean grotesqueGuardiansPrayerFlick() {
    return true;
  }

  @ConfigItem(
      keyName = "alchemicalHydraPrayerFlick",
      name = "Alchemical Hydra Auto Prayer Flick",
      description = "Automatically swap prayers against Alchemical Hydra",
      section = "prayerConfig",
      position = 317)
  default boolean alchemicalHydraPrayerFlick() {
    return true;
  }

  @ConfigItem(
      keyName = "bones",
      name = "Bones",
      description = "Choose which bones to auto-bury.",
      section = "bonesConfig",
      position = 401)
  default Bones bones() {
    return Bones.BIG_BONES;
  }

  @ConfigItem(
      keyName = "ashes",
      name = "Ashes",
      description = "Choose which ashes to auto-scatter.",
      section = "bonesConfig",
      position = 402)
  default Ashes ashes() {
    return Ashes.VILE_ASHES;
  }

  @ConfigItem(
      keyName = "allBelow",
      name = "Bury/scatter all below threshold",
      description =
          "Bury/scatter all the bones/ashes of lower value including the chosen bones/ashes.",
      section = "bonesConfig",
      position = 403)
  default boolean allBelow() {
    return true;
  }

  @ConfigItem(
      keyName = "alchItems",
      name = "Items",
      description =
          "List of items to alch, separated by a semicolon. You can use a part of the name.",
      section = "alchConfig",
      position = 501)
  default String alchItems() {
    return "Rune arrow";
  }

  @ConfigItem(
      keyName = "alchItemsBlacklist",
      name = "Items blacklist",
      description =
          "List of items to NOT alch, separated by a semicolon. You can use a part of the name. "
          + "This is useful if you want to alch all rune items but not arrows for example.",
      section = "alchConfig",
      position = 502)
  default String alchItemsBlacklist() {
    return "ore; bar; arrow";
  }

  @ConfigItem(
      keyName = "alchNoted",
      name = "Alch noted",
      description = "Enable alching of noted items.",
      section = "alchConfig",
      position = 503)
  default boolean alchNoted() {
    return false;
  }

  @ConfigItem(
      keyName = "autoalchHotkey",
      name = "Hotkey",
      description = "Press this key to toggle auto alching.",
      section = "alchConfig",
      position = 503)
  default Keybind autoalchHotkey() {
    return new Keybind(KeyEvent.VK_F9, InputEvent.CTRL_DOWN_MASK);
  }

  @ConfigItem(
      keyName = "disableAfterTask",
      name = "Disable after task",
      description = "Disable alching after finishing Slayer task.",
      section = "alchConfig",
      position = 504)
  default boolean disableAfterTask() {
    return true;
  }

  @ConfigItem(
      keyName = "meleeGear",
      name = "Melee gear",
      description =
          "Names or IDs of your melee gear. Names match any item containing the name, meaning "
          + "'Dharok's platelegs' matches all degradation values. Separate by line, semicolon or "
          + "comma.",
      section = "swapConfig",
      position = 601)
  default String meleeGear() {
    return "";
  }

  @ConfigItem(
      keyName = "meleeHotkey",
      name = "Melee hotkey",
      description = "Hotkey to manually swap gear",
      section = "swapConfig",
      position = 602)
  default Keybind meleeHotkey() {
    return new Keybind(KeyEvent.VK_F1, InputEvent.CTRL_DOWN_MASK);
  }

  @ConfigItem(
      keyName = "rangedGear",
      name = "Ranged gear",
      description =
          "Names or IDs of your ranged gear. Names match any item containing the name, meaning "
          + "'Dharok's platelegs' matches all degradation values. Separate by line, semicolon or "
          + "comma.",
      section = "swapConfig",
      position = 603)
  default String rangedGear() {
    return "";
  }

  @ConfigItem(
      keyName = "rangedHotkey",
      name = "Ranged hotkey",
      description = "Hotkey to manually swap gear",
      section = "swapConfig",
      position = 604)
  default Keybind rangedHotkey() {
    return new Keybind(KeyEvent.VK_F2, InputEvent.CTRL_DOWN_MASK);
  }

  @ConfigItem(
      keyName = "magicGear",
      name = "Magic gear",
      description =
          "Names or IDs of your magic gear. Names match any item containing the name, meaning "
          + "'Dharok's platelegs' matches all degradation values. Separate by line, semicolon or "
          + "comma.",
      section = "swapConfig",
      position = 605)
  default String magicGear() {
    return "";
  }

  @ConfigItem(
      keyName = "magicHotkey",
      name = "Magic hotkey",
      description = "Hotkey to manually swap gear",
      section = "swapConfig",
      position = 606)
  default Keybind magicHotkey() {
    return new Keybind(KeyEvent.VK_F3, InputEvent.CTRL_DOWN_MASK);
  }

  @ConfigItem(
      keyName = "swapOffensivePrayers",
      name = "Swap offensive prayers",
      description = "Enable to swap offensive prayers to match the gear",
      section = "swapConfig",
      position = 607)
  default boolean swapOffensivePrayers() {
    return true;
  }

  @ConfigItem(
      keyName = "swapDefensivePrayers",
      name = "Swap defensive prayers",
      description =
          "Enable to swap defensive prayers to match the gear and combat triangle (eg. will pray "
          + "protect from ranged when equipping melee)",
      section = "swapConfig",
      position = 608)
  default boolean swapDefensivePrayers() {
    return false;
  }

  @ConfigItem(
      keyName = "meleePrayer",
      name = "Melee set prayer",
      description = "Select overhead prayer to enable when swapping to melee set",
      section = "swapConfig",
      hidden = true,
      unhide = "swapDefensivePrayers",
      position = 609)
  default OverheadPrayer meleePrayer() {
    return OverheadPrayer.PROTECT_FROM_MISSILES;
  }

  @ConfigItem(
      keyName = "rangedPrayer",
      name = "Ranged set prayer",
      description = "Select overhead prayer to enable when swapping to ranged set",
      section = "swapConfig",
      hidden = true,
      unhide = "swapDefensivePrayers",
      position = 610)
  default OverheadPrayer rangedPrayer() {
    return OverheadPrayer.PROTECT_FROM_MAGIC;
  }

  @ConfigItem(
      keyName = "magicPrayer",
      name = "Magic set prayer",
      description = "Select overhead prayer to enable when swapping to magic set",
      section = "swapConfig",
      hidden = true,
      unhide = "swapDefensivePrayers",
      position = 611)
  default OverheadPrayer magicPrayer() {
    return OverheadPrayer.PROTECT_FROM_MELEE;
  }

  @ConfigItem(
      keyName = "autoSwap",
      name = "Auto swap",
      description = "Automatically swap sets depending on prayer of target",
      section = "swapConfig",
      position = 612)
  default boolean autoSwap() {
    return true;
  }

  @ConfigItem(
      keyName = "autoSwapZulrah",
      name = "Auto swap Zulrah",
      description = "Automatically swap sets against Zulrah",
      section = "swapConfig",
      position = 613)
  default boolean autoSwapZulrah() {
    return true;
  }

  @ConfigItem(
      keyName = "autoSwapGrotesqueGuardians",
      name = "Auto swap Grotesque Guardians",
      description = "Automatically swap sets against Grotesque Guardians",
      section = "swapConfig",
      position = 614)
  default boolean autoSwapGrotesqueGuardians() {
    return true;
  }

  @ConfigItem(
      keyName = "pkTeleport",
      name = "PK teleport hotkey",
      description =
          "Pressing the hotkey will toggle watching for skulled players or players attacking you "
          + "and try to teleport away. Requires an equipped charged amulet of glory.",
      section = "miscConfig",
      position = 701)
  default Keybind pkTeleport() {
    return new Keybind(KeyEvent.VK_F1, InputEvent.CTRL_DOWN_MASK);
  }

  @ConfigItem(
      keyName = "tpOnDangerousPlayer",
      name = "TP on dangerous player",
      description = "Attempt to teleport when a skulled player within level range appears.",
      section = "miscConfig",
      position = 702)
  default boolean tpOnDangerousPlayer() {
    return true;
  }

  @ConfigItem(
      keyName = "tpOnPlayerAttack",
      name = "TP on player attack",
      description = "Attempt to teleport when we're being attacked by a player.",
      section = "miscConfig",
      position = 703)
  default boolean tpOnPlayerAttack() {
    return true;
  }

  @ConfigItem(
      keyName = "tpHotkey",
      name = "TP hotkey",
      description = "Hotkey for emergency teleport",
      section = "miscConfig",
      position = 704)
  default Keybind tpHotkey() {
    return new Keybind(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK);
  }

  @ConfigItem(
      keyName = "vengeanceHotkey",
      name = "Vengeance hotkey",
      description = "Hotkey for casting Vengeance",
      section = "miscConfig",
      position = 705)
  default Keybind vengeanceHotkey() {
    return new Keybind(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK);
  }

  @ConfigItem(
      keyName = "zulrahAutoVengeance",
      name = "Zulrah auto Vengeance",
      description = "Automatically cast vengeance on Zulrah ranged attack during magic phase",
      section = "miscConfig",
      position = 706)
  default boolean zulrahAutoVengeance() {
    return false;
  }

  @ConfigItem(
      keyName = "attackStyleOneHotkey",
      name = "Attack style 1",
      description = "Hotkey to switch to attack style 1",
      section = "miscConfig",
      position = 710)
  default Keybind attackStyleOneHotkey() {
    return new Keybind(KeyEvent.VK_NUMPAD1, InputEvent.CTRL_DOWN_MASK);
  }

  @ConfigItem(
      keyName = "attackStyleTwoHotkey",
      name = "Attack style 2",
      description = "Hotkey to switch to attack style 2",
      section = "miscConfig",
      position = 711)
  default Keybind attackStyleTwoHotkey() {
    return new Keybind(KeyEvent.VK_NUMPAD2, InputEvent.CTRL_DOWN_MASK);
  }

  @ConfigItem(
      keyName = "attackStyleThreeHotkey",
      name = "Attack style 3",
      description = "Hotkey to switch to attack style 3",
      section = "miscConfig",
      position = 712)
  default Keybind attackStyleThreeHotkey() {
    return new Keybind(KeyEvent.VK_NUMPAD3, InputEvent.CTRL_DOWN_MASK);
  }

  @ConfigItem(
      keyName = "attackStyleFourHotkey",
      name = "Attack style 4",
      description = "Hotkey to switch to attack style 4",
      section = "miscConfig",
      position = 713)
  default Keybind attackStyleFourHotkey() {
    return new Keybind(KeyEvent.VK_NUMPAD4, InputEvent.CTRL_DOWN_MASK);
  }
}
