package io.reisub.unethicalite.woodcutting;

import com.google.inject.Provides;
import io.reisub.unethicalite.utils.TickPlugin;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.woodcutting.tasks.Chop;
import io.reisub.unethicalite.woodcutting.tasks.Drop;
import io.reisub.unethicalite.woodcutting.tasks.GoToBank;
import io.reisub.unethicalite.woodcutting.tasks.GoToChoppingArea;
import io.reisub.unethicalite.woodcutting.tasks.HandleBank;
import io.reisub.unethicalite.woodcutting.tasks.MoveToRespawning;
import io.reisub.unethicalite.woodcutting.tasks.PickupNest;
import io.reisub.unethicalite.woodcutting.tasks.UseSpecial;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
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
public class Woodcutting extends TickPlugin {

  @Inject
  private Config config;
  @Getter
  @Setter
  private int lastBankTick;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  public void onStart() {
    super.onStart();

    final Chop chop = injector.getInstance(Chop.class);
    chop.setCurrentTreePosition(null);

    addTask(Drop.class);
    addTask(HandleBank.class);
    addTask(GoToBank.class);
    addTask(GoToChoppingArea.class);
    addTask(PickupNest.class);
    addTask(chop);
    addTask(UseSpecial.class);
    addTask(MoveToRespawning.class);
  }
}
