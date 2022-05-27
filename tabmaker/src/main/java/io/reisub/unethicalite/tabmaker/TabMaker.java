package io.reisub.unethicalite.tabmaker;

import com.google.inject.Provides;
import io.reisub.unethicalite.tabmaker.tasks.HandleBank;
import io.reisub.unethicalite.tabmaker.tasks.MakeTabs;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.enums.Activity;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.items.Inventory;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Tab Maker",
    description = "Put it on my tab",
    enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class TabMaker extends TickScript {
  @Inject
  private Config config;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  protected void onStart() {
    super.onStart();

    addTask(HandleBank.class);
    addTask(MakeTabs.class);
  }

  @Subscribe
  private void onItemContainerChanged(ItemContainerChanged event) {
    if (!isRunning()) {
      return;
    }

    if (currentActivity == Activity.MAKING_TABS && !Inventory.contains(ItemID.SOFT_CLAY)) {
      setActivity(Activity.IDLE);
    }
  }
}
