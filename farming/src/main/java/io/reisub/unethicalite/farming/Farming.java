package io.reisub.unethicalite.farming;

import com.google.inject.Provides;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.game.GameThread;
import dev.hoot.api.items.Inventory;
import dev.hoot.bot.managers.Static;
import io.reisub.unethicalite.farming.tasks.Clear;
import io.reisub.unethicalite.farming.tasks.Cure;
import io.reisub.unethicalite.farming.tasks.DepositTools;
import io.reisub.unethicalite.farming.tasks.GoToPatch;
import io.reisub.unethicalite.farming.tasks.HandleBank;
import io.reisub.unethicalite.farming.tasks.Note;
import io.reisub.unethicalite.farming.tasks.Pick;
import io.reisub.unethicalite.farming.tasks.Plant;
import io.reisub.unethicalite.farming.tasks.WithdrawTools;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.Predicates;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Item;
import net.runelite.api.MenuAction;
import net.runelite.api.TileObject;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyListener;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

import javax.inject.Inject;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Queue;

@PluginDescriptor(
		name = "Chaos Farming",
		description = "It's not much but it's honest work",
		enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class Farming extends TickScript implements KeyListener {
	@Inject
	private Config config;

	@Provides
	public Config getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Config.class);
	}

	@Getter
	private final Queue<Location> locationQueue = new LinkedList<>();

	@Getter
	private Location currentLocation;

	@Override
	protected void onStart() {
		super.onStart();

		locationQueue.add(Location.FARMING_GUILD);
		locationQueue.add(Location.ARDOUGNE);
		locationQueue.add(Location.CATHERBY);
		locationQueue.add(Location.FALADOR);
		locationQueue.add(Location.PORT_PHASMATYS);
		locationQueue.add(Location.HOSIDIUS);
		locationQueue.add(Location.HARMONY_ISLAND);
		locationQueue.add(Location.TROLL_STRONGHOLD);
		locationQueue.add(Location.WEISS);

		addTask(HandleBank.class);
		addTask(GoToPatch.class);
		addTask(WithdrawTools.class);
		addTask(Note.class);
		addTask(Cure.class);
		addTask(Clear.class);
		addTask(Pick.class);
		addTask(Plant.class);
		addTask(DepositTools.class);
	}

	@Override
	protected void onStop() {
		super.onStop();

		for (Location location : Location.values()) {
			location.setDone(false);
		}

		locationQueue.clear();
		currentLocation = null;
	}

	@Subscribe
	private void onGameTick(GameTick event) {
		if (locationQueue.isEmpty() || !isRunning()) {
			return;
		}

		if (currentLocation == null || currentLocation.isDone()) {
			while (!locationQueue.isEmpty()) {
				Location location = locationQueue.poll();

				if (location != null && location.isEnabled(config)) {
					currentLocation = location;
					break;
				}
			}
		}
	}

	@Subscribe
	private void onMenuEntryAdded(MenuEntryAdded event) {
		if (!config.oneClickMode() || event.getType() == MenuAction.EXAMINE_ITEM.getId()) {
			return;
		}

		if (TileObjects.getNearest(Predicates.ids(OneClick.ONE_CLICK_MAP.get(event.getIdentifier()))) == null) {
			return;
		}

		if (OneClick.ONE_CLICK_MAP.containsKey(event.getIdentifier())) {
			Static.getClient().insertMenuItem(
					OneClick.ONE_CLICK_FARMING,
					"",
					MenuAction.UNKNOWN.getId(),
					event.getIdentifier(),
					event.getActionParam0(),
					event.getActionParam1(),
					true
			);
		}
	}

	@Subscribe
	private void onMenuOptionClicked(MenuOptionClicked event) {
		if (!event.getMenuOption().equals(OneClick.ONE_CLICK_FARMING)) {
			return;
		}

		Item item = Inventory.getFirst(event.getId());
		TileObject nearest = TileObjects.getNearest(Predicates.ids(OneClick.ONE_CLICK_MAP.get(event.getId())));
		if (item == null || nearest == null) {
			return;
		}

		GameThread.invoke(() -> item.useOn(nearest));
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (config.farmingHotkey().matches(e)) {
			e.consume();
			start();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}
