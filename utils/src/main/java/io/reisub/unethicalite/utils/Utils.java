package io.reisub.unethicalite.utils;

import com.google.inject.Provides;
import java.util.Collection;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.Game;
import net.unethicalite.client.Static;
import org.pf4j.Extension;

@PluginDescriptor(name = "Chaos Utils", description = "Utilities for Chaos scripts")
@Slf4j
@Singleton
@Extension
public class Utils extends Plugin {
  public static boolean isLoggedIn() {
    return Game.getClient() != null && Game.getState() == GameState.LOGGED_IN;
  }

  public static boolean isInRegion(int... regionIds) {
    Player player = Players.getLocal();

    if (player.getWorldLocation() == null) {
      return false;
    }

    for (int regionId : regionIds) {
      if (player.getWorldLocation().getRegionID() == regionId) {
        return true;
      }
    }

    return false;
  }

  public static boolean isInRegion(Collection<Integer> regionIds) {
    return isInRegion(regionIds.stream().mapToInt(i -> i).toArray());
  }

  public static boolean isInMapRegion(int... regionIds) {
    for (int id : Static.getClient().getMapRegions()) {
      for (int regionId : regionIds) {
        if (id == regionId) {
          return true;
        }
      }
    }

    return false;
  }

  public static boolean isInMapRegion(Collection<Integer> regionIds) {
    return isInMapRegion(regionIds.stream().mapToInt(i -> i).toArray());
  }

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  public void startUp() {
    log.info(this.getName() + " started");
  }

  @Override
  protected void shutDown() {
    log.info(this.getName() + " stopped");
  }
}
