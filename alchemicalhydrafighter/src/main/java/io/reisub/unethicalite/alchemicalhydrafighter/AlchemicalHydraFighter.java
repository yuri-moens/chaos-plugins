package io.reisub.unethicalite.alchemicalhydrafighter;

import com.google.inject.Provides;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ConfigList;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Alchemical Hydra Fighter",
    description = "Fights the Alchemical Hydra",
    enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class AlchemicalHydraFighter extends TickScript {

  @Inject
  private Config config;
  @Getter
  private ConfigList equipment;
  @Getter
  private ConfigList inventory;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  protected void onStart() {
    equipment = ConfigList.parseList(config.equipment());
    inventory = ConfigList.parseList(config.inventory());

    super.onStart();

    // addTask();
  }

  @Subscribe
  private void onConfigChanged(ConfigChanged event) {
    if (!event.getGroup().equals("chaosalchemicalhydrafighter")) {
      return;
    }

    if (event.getKey().equals("equipment")) {
      equipment = ConfigList.parseList(config.equipment());
    } else if (event.getKey().equals("inventory")) {
      inventory = ConfigList.parseList(config.inventory());
    }
  }
}
