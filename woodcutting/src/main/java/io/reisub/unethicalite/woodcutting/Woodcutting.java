package io.reisub.unethicalite.woodcutting;

import com.google.inject.Provides;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.woodcutting.tasks.Chop;
import io.reisub.unethicalite.woodcutting.tasks.Drop;
import io.reisub.unethicalite.woodcutting.tasks.GoToBank;
import io.reisub.unethicalite.woodcutting.tasks.GoToChoppingArea;
import io.reisub.unethicalite.woodcutting.tasks.HandleBank;
import io.reisub.unethicalite.woodcutting.tasks.MoveToRespawning;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Woodcutting",
    description = "I hear digging but I don't hear chopping",
    enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class Woodcutting extends TickScript {

  @Inject
  private Config config;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  protected void onStart() {
    super.onStart();

    final Chop chop = injector.getInstance(Chop.class);
    chop.setCurrentTreePosition(null);

    addTask(Drop.class);
    addTask(HandleBank.class);
    addTask(GoToBank.class);
    addTask(GoToChoppingArea.class);
    addTask(chop);
    addTask(MoveToRespawning.class);
  }
}
