package io.reisub.unethicalite.autodropper;

import com.google.inject.Provides;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ConfigList;
import io.reisub.unethicalite.utils.api.Predicates;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
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
import net.unethicalite.api.game.Game;
import net.unethicalite.api.items.Inventory;
import org.pf4j.Extension;

@Extension
@PluginDependency(Utils.class)
@PluginDescriptor(
    name = "Chaos Auto Dropper",
    description = "Serial litterer",
    enabledByDefault = false)
@Slf4j
public class AutoDropper extends Plugin implements KeyListener {
  @Inject private Config config;

  @Inject private KeyManager keyManager;
  private ScheduledExecutorService executor;
  private ConfigList configList;

  @Provides
  Config provideConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  protected void startUp() {
    log.info("Starting Chaos Auto Dropper");

    configList = ConfigList.parseList(config.items());

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
    if (Game.getState() != GameState.LOGGED_IN
        || config.dropMethod() == DropMethod.NONE
        || event.getContainerId() != InventoryID.INVENTORY.getId()) {
      return;
    }

    if (config.dropMethod() == DropMethod.ON_ADD) {
      drop();
    } else if (config.dropMethod() == DropMethod.ON_FULL_INVENTORY && Inventory.isFull()) {
      drop();
    }
  }

  @Subscribe
  private void onConfigChanged(ConfigChanged event) {
    String name = this.getName().replaceAll(" ", "").toLowerCase(Locale.ROOT);

    if (event.getGroup().equals(name) && event.getKey().startsWith("items")) {
      configList = ConfigList.parseList(config.items());
    }
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
    List<Item> items = Inventory.getAll(Predicates.itemConfigList(configList));

    for (Item item : items) {
      item.interact("Drop");
    }
  }
}
