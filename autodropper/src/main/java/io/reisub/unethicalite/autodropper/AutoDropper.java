package io.reisub.unethicalite.autodropper;

import com.google.inject.Provides;
import dev.hoot.api.commons.Rand;
import dev.hoot.api.game.Game;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.packets.ItemPackets;
import io.reisub.unethicalite.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

import javax.inject.Inject;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Extension
@PluginDependency(Utils.class)
@PluginDescriptor(
	name = "Chaos Auto Dropper",
	description = "Serial litterer",
	enabledByDefault = false
)
@Slf4j
public class AutoDropper extends Plugin implements KeyListener {
	@Inject
	private Config config;

	@Inject
	private KeyManager keyManager;

	@Provides
    Config provideConfig(ConfigManager configManager) {
		return configManager.getConfig(Config.class);
	}

	private ScheduledExecutorService executor;
	private String[] itemNames;
	private int[] itemIds;

	@Override
	protected void startUp() {
		log.info("Starting Chaos Auto Dropper");

		itemNames = parseNames();
		itemIds = parseIds();

		keyManager.registerKeyListener(this);
		executor = Executors.newSingleThreadScheduledExecutor();
	}

	@Override
	protected void shutDown() {
		log.info("Stopping Chaos Auto Dropper");

		keyManager.unregisterKeyListener(this);
		executor.shutdownNow();
	}

	@Subscribe
	private void onItemContainerChanged(ItemContainerChanged event) {
		if (Game.getState() != GameState.LOGGED_IN || config.dropMethod() == DropMethod.NONE || event.getContainerId() != InventoryID.INVENTORY.getId()) return;

		if (config.dropMethod() == DropMethod.ON_ADD) {
			drop();
		} else if (config.dropMethod() == DropMethod.ON_FULL_INVENTORY && Inventory.isFull()) {
			drop();
		}
	}

	@Subscribe
	private void onConfigChanged(ConfigChanged event) {
		String name = this.getName().replaceAll(" ", "").toLowerCase(Locale.ROOT);

		if (event.getGroup().equals(name) && event.getKey().startsWith("item")) {
			itemNames = parseNames();
			itemIds = parseIds();
		}
	}

	private String[] parseNames() {
		String[] itemNames = config.itemNames().split(";");
		String[] seedNames = config.seedNames().split(";");
		String[] names = new String[itemNames.length + seedNames.length];

		int i = 0;

		for (String name : itemNames) {
			names[i++] = name.trim();
		}

		for (String name : seedNames) {
			names[i++] = name.trim();
		}

		return names;
	}

	private int[] parseIds() {
		if (config.itemIds().equals("")) return new int[]{};

		String[] idsStr = config.itemIds().split(";");
		int[] ids = new int[idsStr.length];

		for (int i = 0; i < idsStr.length; i++) {
			ids[i] = Integer.parseInt(idsStr[i].trim());
		}

		return ids;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if (config.dropEnableHotkey() && config.dropHotkey().matches(e)) {
			drop();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	private void drop() {
		List<Item> items = Inventory.getAll(itemNames);
		items.addAll(Inventory.getAll(itemIds));

		for (Item item : items) {
			ItemPackets.itemAction(item, "Drop");
		}
	}
}