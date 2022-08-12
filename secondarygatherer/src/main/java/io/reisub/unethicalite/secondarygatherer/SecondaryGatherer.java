package io.reisub.unethicalite.secondarygatherer;

import com.google.inject.Provides;
import io.reisub.unethicalite.combathelper.CombatHelper;
import io.reisub.unethicalite.secondarygatherer.tasks.GoToScales;
import io.reisub.unethicalite.secondarygatherer.tasks.GrabWine;
import io.reisub.unethicalite.secondarygatherer.tasks.HandleBank;
import io.reisub.unethicalite.secondarygatherer.tasks.Hop;
import io.reisub.unethicalite.secondarygatherer.tasks.TakeScale;
import io.reisub.unethicalite.secondarygatherer.tasks.TeleportCraftingGuild;
import io.reisub.unethicalite.secondarygatherer.tasks.TeleportMythsGuild;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;
import org.slf4j.Logger;

@PluginDescriptor(
    name = "Chaos Secondary Gatherer",
    description = "Gathers herblore secondaries for you",
    enabledByDefault = false)
@PluginDependency(Utils.class)
@PluginDependency(CombatHelper.class)
@Slf4j
@Extension
public class SecondaryGatherer extends TickScript {

  @Inject
  private Config config;

  @Inject
  @Getter
  private CombatHelper combatHelper;

  public static final int MYTHS_GUILD_REGION = 9772;
  public static final int MYTHS_GUILD_DUMGEON_REGION = 7820;

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

    addTask(Hop.class);
    addTask(GrabWine.class);

    addTask(HandleBank.class);
    addTask(TeleportMythsGuild.class);
    addTask(GoToScales.class);
    addTask(TakeScale.class);
    addTask(TeleportCraftingGuild.class);
  }
}
