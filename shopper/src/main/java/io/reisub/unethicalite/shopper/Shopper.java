package io.reisub.unethicalite.shopper;

import com.google.inject.Provides;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@PluginDescriptor(
		name = "Chaos Shopper",
		description = "Hops worlds and buys stuff from NPCs",
		enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class Shopper extends TickScript {
	@Inject
	private Config config;

	@Provides
	public Config getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Config.class);
	}

	@Getter
	private List<Item> items;

	@Getter
	private WorldPoint bankLocation;

	@Getter
	private WorldPoint npcLocation;

	@Override
	protected void onStart() {
		super.onStart();

		loadItems();
		loadLocations();

		// addTask();

	}

	private void loadItems() {
		items = new ArrayList<>();

		if (config.itemOneEnabled()) {
			items.add(new Item(config.itemOneId(), config.itemOneAmount(), config.itemOneMinInStore()));
		}

		if (config.itemTwoEnabled()) {
			items.add(new Item(config.itemTwoId(), config.itemTwoAmount(), config.itemTwoMinInStore()));
		}

		if (config.itemThreeEnabled()) {
			items.add(new Item(config.itemThreeId(), config.itemThreeAmount(), config.itemThreeMinInStore()));
		}

		if (config.itemFourEnabled()) {
			items.add(new Item(config.itemFourId(), config.itemFourAmount(), config.itemFourMinInStore()));
		}

		if (config.itemFiveEnabled()) {
			items.add(new Item(config.itemFiveId(), config.itemFiveAmount(), config.itemFiveMinInStore()));
		}
	}

	private void loadLocations() {
		if (
				config.bankLocation().equals("")
						|| config.bankLocation().equals("0,0,0")
						|| config.npcLocation().equals("")
						|| config.npcLocation().equals("0,0,0")
		) {
			return;
		}

		String[] bankLocationSplit = config.bankLocation().split(",");
		String[] npcLocationSplit = config.npcLocation().split(",");

		try {
			bankLocation = new WorldPoint(
					Integer.parseInt(bankLocationSplit[0]),
					Integer.parseInt(bankLocationSplit[1]),
					Integer.parseInt(bankLocationSplit[2])
			);

			npcLocation = new WorldPoint(
					Integer.parseInt(npcLocationSplit[0]),
					Integer.parseInt(npcLocationSplit[1]),
					Integer.parseInt(npcLocationSplit[2])
			);
		} catch (Exception e) {
			log.error("Bank or NPC location format is invalid.");
		}
	}
}
