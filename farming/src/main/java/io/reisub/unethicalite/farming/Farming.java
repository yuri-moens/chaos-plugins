package io.reisub.unethicalite.farming;

import com.google.inject.Provides;
import io.reisub.unethicalite.farming.tasks.Clear;
import io.reisub.unethicalite.farming.tasks.DepositTools;
import io.reisub.unethicalite.farming.tasks.GoToPatch;
import io.reisub.unethicalite.farming.tasks.HandleBank;
import io.reisub.unethicalite.farming.tasks.Note;
import io.reisub.unethicalite.farming.tasks.Pick;
import io.reisub.unethicalite.farming.tasks.Plant;
import io.reisub.unethicalite.farming.tasks.WithdrawTools;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
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
		addTask(Clear.class);
		addTask(Pick.class);
		addTask(Plant.class);
		addTask(Note.class);
		addTask(DepositTools.class);
	}

	@Override
	protected void onStop() {
		super.onStop();

		locationQueue.clear();
	}

	@Subscribe
	private void onGameTick(GameTick event) {
		if (locationQueue.isEmpty()) {
			return;
		}

		if (currentLocation == null || currentLocation.isDone()) {
			boolean newLocation = false;

			while (!newLocation) {
				Location location = locationQueue.poll();

				if (location == null) {
					stop("Finished all farming locations. Stopping script.");
				} else if (location.isEnabled(config)) {
					currentLocation = location;
					newLocation = true;
				}
			}
		}
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
