package io.reisub.unethicalite.fletching;

import com.google.inject.Provides;
import io.reisub.unethicalite.fletching.tasks.MakeArrows;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
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
  @Inject private Config config;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  protected void onStart() {
    super.onStart();

    addTask(MakeArrows.class);
  }
}
