package io.reisub.unethicalite.mining;

import com.google.inject.Provides;
import io.reisub.unethicalite.mining.tasks.CastHumidify;
import io.reisub.unethicalite.mining.tasks.Deposit;
import io.reisub.unethicalite.mining.tasks.GoToMiningArea;
import io.reisub.unethicalite.mining.tasks.Mine;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Mining",
    description = "Diggy, diggy hole",
    enabledByDefault = false)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class Mining extends TickScript {
  @Inject private Config config;
  @Getter @Setter private boolean arrived;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  protected void onStart() {
    super.onStart();

    addTask(CastHumidify.class);
    addTask(Deposit.class);
    addTask(GoToMiningArea.class);
    addTask(Mine.class);
  }
}
