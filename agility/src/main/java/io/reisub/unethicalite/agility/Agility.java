package io.reisub.unethicalite.agility;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import io.reisub.unethicalite.agility.tasks.Alch;
import io.reisub.unethicalite.agility.tasks.HandleObstacle;
import io.reisub.unethicalite.agility.tasks.PickupMark;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import java.util.Set;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Agility",
    description = "Hippity hoppity, jumps on your property",
    enabledByDefault = false)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class Agility extends TickScript {
  public static final Set<WorldPoint> DELAY_POINTS =
      ImmutableSet.of(new WorldPoint(3363, 2998, 0), new WorldPoint(2653, 3676, 0));
  @Inject private Config config;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  protected void onStart() {
    super.onStart();

    addTask(PickupMark.class);
    addTask(HandleObstacle.class);
    addTask(Alch.class);
  }
}
