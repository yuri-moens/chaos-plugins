package io.reisub.unethicalite.shopper;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("chaosshopper")
public interface Config extends net.runelite.client.config.Config {
	@ConfigItem(
			keyName = "npcName",
			name = "NPC name",
			description = "Name of the NPC to buy from",
			position = 0
	)
	default String npcName() {
		return "Ordan";
	}

	@ConfigItem(
			keyName = "npcLocation",
			name = "NPC location",
			description = "Location near the NPC in format x,y,z. This is only required if the bank and NPC are far away from each other.",
			position = 1
	)
	default String npcLocation() {
		return "0,0,0";
	}

	@ConfigItem(
			keyName = "bankLocation",
			name = "Bank location",
			description = "Location near the bank in format x,y,z. This is only required if the bank and NPC are far away from each other.",
			position = 2
	)
	default String bankLocation() {
		return "0,0,0";
	}

	@ConfigItem(
			keyName = "disableRunFromShop",
			name = "Disable run from shop",
			description = "Disable run when going from store to bank",
			position = 3
	)
	default boolean disableRunFromShop() {
		return false;
	}

	@ConfigItem(
			keyName = "openPacks",
			name = "Open packs",
			description = "Open packs like vials or runes",
			position = 4
	)
	default boolean openPacks() {
		return true;
	}

	@ConfigSection(
			keyName = "itemOne",
			name = "Item 1",
			description = "Configure the item to buy",
			position = 10
	)
	String itemOne = "itemOne";

	@ConfigItem(
			keyName = "itemOneEnabled",
			name = "Enable",
			description = "Enable the buying of this item",
			section = "itemOne",
			position = 11
	)
	default boolean itemOneEnabled() {
		return true;
	}

	@ConfigItem(
			keyName = "itemOneId",
			name = "ID",
			description = "ID of the item",
			section = "itemOne",
			position = 12
	)
	default int itemOneId() {
		return 0;
	}

	@ConfigItem(
			keyName = "itemOneAmount",
			name = "Amount",
			description = "Amount of the item to buy",
			section = "itemOne",
			position = 13
	)
	default int itemOneAmount() {
		return 0;
	}

	@ConfigItem(
			keyName = "itemOneMinInStore",
			name = "Min in store",
			description = "Amount to keep in store to prevent overpaying",
			section = "itemOne",
			position = 14
	)
	default int itemOneMinInStore() {
		return 0;
	}

	@ConfigSection(
			keyName = "itemTwo",
			name = "Item 2",
			description = "Configure the item to buy",
			closedByDefault = true,
			position = 20
	)
	String itemTwo = "itemTwo";

	@ConfigItem(
			keyName = "itemTwoEnabled",
			name = "Enable",
			description = "Enable the buying of this item",
			section = "itemTwo",
			position = 21
	)
	default boolean itemTwoEnabled() {
		return false;
	}

	@ConfigItem(
			keyName = "itemTwoId",
			name = "ID",
			description = "ID of the item",
			section = "itemTwo",
			position = 22
	)
	default int itemTwoId() {
		return 0;
	}

	@ConfigItem(
			keyName = "itemTwoAmount",
			name = "Amount",
			description = "Amount of the item to buy",
			section = "itemTwo",
			position = 23
	)
	default int itemTwoAmount() {
		return 0;
	}

	@ConfigItem(
			keyName = "itemTwoMinInStore",
			name = "Min in store",
			description = "Amount to keep in store to prevent overpaying",
			section = "itemTwo",
			position = 24
	)
	default int itemTwoMinInStore() {
		return 0;
	}

	@ConfigSection(
			keyName = "itemThree",
			name = "Item 3",
			description = "Configure the item to buy",
			closedByDefault = true,
			position = 30
	)
	String itemThree = "itemThree";

	@ConfigItem(
			keyName = "itemThreeEnabled",
			name = "Enable",
			description = "Enable the buying of this item",
			section = "itemThree",
			position = 31
	)
	default boolean itemThreeEnabled() {
		return false;
	}

	@ConfigItem(
			keyName = "itemThreeId",
			name = "ID",
			description = "ID of the item",
			section = "itemThree",
			position = 32
	)
	default int itemThreeId() {
		return 0;
	}

	@ConfigItem(
			keyName = "itemThreeAmount",
			name = "Amount",
			description = "Amount of the item to buy",
			section = "itemThree",
			position = 33
	)
	default int itemThreeAmount() {
		return 0;
	}

	@ConfigItem(
			keyName = "itemThreeMinInStore",
			name = "Min in store",
			description = "Amount to keep in store to prevent overpaying",
			section = "itemThree",
			position = 34
	)
	default int itemThreeMinInStore() {
		return 0;
	}

	@ConfigSection(
			keyName = "itemFour",
			name = "Item 4",
			description = "Configure the item to buy",
			closedByDefault = true,
			position = 40
	)
	String itemFour = "itemFour";

	@ConfigItem(
			keyName = "itemFourEnabled",
			name = "Enable",
			description = "Enable the buying of this item",
			section = "itemFour",
			position = 41
	)
	default boolean itemFourEnabled() {
		return false;
	}

	@ConfigItem(
			keyName = "itemFourId",
			name = "ID",
			description = "ID of the item",
			section = "itemFour",
			position = 42
	)
	default int itemFourId() {
		return 0;
	}

	@ConfigItem(
			keyName = "itemFourAmount",
			name = "Amount",
			description = "Amount of the item to buy",
			section = "itemFour",
			position = 43
	)
	default int itemFourAmount() {
		return 0;
	}

	@ConfigItem(
			keyName = "itemFourMinInStore",
			name = "Min in store",
			description = "Amount to keep in store to prevent overpaying",
			section = "itemFour",
			position = 44
	)
	default int itemFourMinInStore() {
		return 0;
	}

	@ConfigSection(
			keyName = "itemFive",
			name = "Item 5",
			description = "Configure the item to buy",
			closedByDefault = true,
			position = 50
	)
	String itemFive = "itemFive";

	@ConfigItem(
			keyName = "itemFiveEnabled",
			name = "Enable",
			description = "Enable the buying of this item",
			section = "itemFive",
			position = 51
	)
	default boolean itemFiveEnabled() {
		return false;
	}

	@ConfigItem(
			keyName = "itemFiveId",
			name = "ID",
			description = "ID of the item",
			section = "itemFive",
			position = 52
	)
	default int itemFiveId() {
		return 0;
	}

	@ConfigItem(
			keyName = "itemFiveAmount",
			name = "Amount",
			description = "Amount of the item to buy",
			section = "itemFive",
			position = 53
	)
	default int itemFiveAmount() {
		return 0;
	}

	@ConfigItem(
			keyName = "itemFiveMinInStore",
			name = "Min in store",
			description = "Amount to keep in store to prevent overpaying",
			section = "itemFive",
			position = 54
	)
	default int itemFiveMinInStore() {
		return 0;
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
