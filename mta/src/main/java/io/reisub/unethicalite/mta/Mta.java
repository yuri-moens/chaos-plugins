package io.reisub.unethicalite.mta;

import com.google.inject.Provides;
import io.reisub.unethicalite.mta.tasks.BonesTo;
import io.reisub.unethicalite.mta.tasks.EatPeach;
import io.reisub.unethicalite.mta.tasks.Enchant;
import io.reisub.unethicalite.mta.tasks.HighAlch;
import io.reisub.unethicalite.mta.tasks.TelekineticGrab;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.client.Static;
import org.pf4j.Extension;
import org.slf4j.Logger;

@PluginDescriptor(
    name = "Chaos MTA",
    description = "Like Hogwarts, but in RuneScape",
    enabledByDefault = false)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class Mta extends TickScript {
  public static final int MTA_REGION = 13462;
  @Inject private Config config;
  private TelekineticGrab telekineticGrab;
  private BonesTo bonesTo;

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

    telekineticGrab = injector.getInstance(TelekineticGrab.class);
    bonesTo = injector.getInstance(BonesTo.class);

    Static.getKeyManager().registerKeyListener(telekineticGrab);
    Static.getKeyManager().registerKeyListener(bonesTo);

    addTask(telekineticGrab);
    addTask(Enchant.class);
    addTask(HighAlch.class);
    addTask(EatPeach.class);
    addTask(bonesTo);
  }

  @Override
  protected void onStop() {
    super.onStop();

    Static.getKeyManager().unregisterKeyListener(telekineticGrab);
    Static.getKeyManager().unregisterKeyListener(bonesTo);
  }
}
