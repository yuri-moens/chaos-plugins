package io.reisub.unethicalite.birdhouse;

import com.google.inject.Provides;
import dev.hoot.api.entities.Players;
import dev.hoot.api.items.Bank;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.widgets.Widgets;
import io.reisub.unethicalite.birdhouse.tasks.*;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Run;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@PluginDescriptor(
		name = "Chaos Bird House",
		description = "Mass bird slaughter",
		enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class BirdHouse extends TickScript {
	@Inject
	private Config config;

	@Provides
	public Config getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Config.class);
	}

	private static final int UNDERWATER_REGION = 15008;

	public static final WorldArea HILL_HOUSE = new WorldArea(3755, 3861, 20, 16, 1);
	public static final WorldPoint ISLAND = new WorldPoint(3769, 3898, 0);
	public static final int TOOL_WIDGET_ID = 125;
	public static final Supplier<Widget> TOOLS = () -> Widgets.get(TOOL_WIDGET_ID, 0);
	public static final Supplier<Widget> CLOSE = () -> Widgets.get(TOOL_WIDGET_ID, 1, 11);

	private final List<Integer> emptied = new ArrayList<>();

	@Override
	protected void onStart() {
		super.onStart();

		emptied.clear();

		tasks.add(new Run());
		tasks.add(new AddSeeds());
		tasks.add(new HandleBank(config));
		tasks.add(new BuildBirdHouse());
		tasks.add(new CraftBirdhouse());
		tasks.add(new EmptyBirdHouse(this));
		tasks.add(new GetTools(this));
		tasks.add(new GoToBirdHouse(this));
		tasks.add(new GoToMushroomMeadow());
		tasks.add(new GoToVerdantValley());
		tasks.add(new GoToIsland(this, config));
		tasks.add(new PickupSpore(this, config));
		tasks.add(new PlantSeaweed(this));
		tasks.add(new NoteSeaweed(this));
		tasks.add(new HarvestSeaweed(this, config));
		tasks.add(new Deposit(this));
	}

	@Subscribe
	private void onGameTick(GameTick event) {
		if (!isLoggedIn()) {
			return;
		}

		if (!isRunning() && HILL_HOUSE.contains(Players.getLocal()) && Inventory.getCount((i) -> Constants.LOG_IDS.contains(i.getId())) == 4) {
			start();
		} else if (isRunning() && Inventory.isEmpty() && !Bank.isOpen()) {
			stop();
		}
	}

	public void emptied(int spaceId) {
		emptied.add(spaceId);
	}

	public boolean isEmptied(int spaceId) {
		return emptied.contains(spaceId);
	}

	public boolean isUnderwater() {
		return Players.getLocal().getWorldLocation().getRegionID() == UNDERWATER_REGION;
	}
}
