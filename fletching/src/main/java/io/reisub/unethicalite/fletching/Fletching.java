package io.reisub.unethicalite.fletching;

import com.google.inject.Provides;
import io.reisub.unethicalite.fletching.data.PluginActivity;
import io.reisub.unethicalite.fletching.tasks.Attach;
import io.reisub.unethicalite.fletching.tasks.Fletch;
import io.reisub.unethicalite.fletching.tasks.HandleBank;
import io.reisub.unethicalite.fletching.tasks.Stringing;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.Activity;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.items.Inventory;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Fletching",
    description = "Y fletch?",
    enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class Fletching extends TickScript {

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
    addTask(Fletch.class);
    addTask(Stringing.class);
    addTask(Attach.class);
  }

  @Subscribe
  private void onItemContainerChanged(ItemContainerChanged event) {
    if (!isRunning()) {
      return;
    }

    if (isCurrentActivity(PluginActivity.FLETCHING)
        && !Inventory.contains(config.logType().getId())) {
      setActivity(Activity.IDLE);
    }
  }
}
