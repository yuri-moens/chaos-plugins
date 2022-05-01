package io.reisub.unethicalite.plankmaker;

import com.google.inject.Provides;
import io.reisub.unethicalite.plankmaker.tasks.GiveLogs;
import io.reisub.unethicalite.plankmaker.tasks.HandleBank;
import io.reisub.unethicalite.plankmaker.tasks.TeleportCamelot;
import io.reisub.unethicalite.plankmaker.tasks.TeleportHouse;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Plank Maker",
    description = "Planking's still hip, right?",
    enabledByDefault = false)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class PlankMaker extends TickScript {
  @Inject private Config config;

  public static final int CAMELOT_REGION = 11062;

  public static int lastGive;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  protected void onStart() {
    super.onStart();

    addTask(HandleBank.class);
    addTask(TeleportHouse.class);
    addTask(GiveLogs.class);
    addTask(TeleportCamelot.class);
  }
}
