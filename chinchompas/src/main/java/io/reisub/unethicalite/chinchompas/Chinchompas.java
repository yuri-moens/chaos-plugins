package io.reisub.unethicalite.chinchompas;

import com.google.inject.Provides;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;
import org.slf4j.Logger;

@PluginDescriptor(
    name = "Chaos Chinchompas",
    description = "Traps chinchompas",
    enabledByDefault = false)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class Chinchompas extends TickScript {

  @Inject
  private Config config;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  public Logger getLogger() {
    return log;
  }

  @Override
  protected void onStart() {
    super.onStart();

    // addTask();
  }
}
