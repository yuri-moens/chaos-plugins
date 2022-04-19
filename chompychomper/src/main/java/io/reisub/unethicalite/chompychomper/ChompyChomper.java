package io.reisub.unethicalite.chompychomper;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import dev.unethicalite.api.entities.Players;
import io.reisub.unethicalite.chompychomper.tasks.Fill;
import io.reisub.unethicalite.chompychomper.tasks.Inflate;
import io.reisub.unethicalite.chompychomper.tasks.Pluck;
import io.reisub.unethicalite.chompychomper.tasks.Shoot;
import io.reisub.unethicalite.chompychomper.tasks.Trap;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.enums.Activity;
import java.util.Set;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.NpcID;
import net.runelite.api.events.InteractingChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Chompy Chomper",
    description = "Chomps chompies",
    enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class ChompyChomper extends TickScript {
  @Inject
  private Config config;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  public static final Set<Integer> FILLED_BELLOW_IDS = ImmutableSet.of(
      ItemID.OGRE_BELLOWS_1,
      ItemID.OGRE_BELLOWS_2,
      ItemID.OGRE_BELLOWS_3
  );

  @Override
  protected void onStart() {
    super.onStart();

    addTask(Shoot.class);
    addTask(Pluck.class);
    addTask(Fill.class);
    addTask(Inflate.class);
    addTask(Trap.class);
  }

  @Subscribe
  private void onInteractingChanged(InteractingChanged event) {
    if (!isRunning() || event.getSource() == null || !event.getSource().equals(Players.getLocal())) {
      return;
    }

    if (event.getTarget() == null) {
      if (currentActivity == Activity.ATTACKING) {
        setActivity(Activity.IDLE);
      }
    } else if (event.getTarget().getId() == NpcID.CHOMPY_BIRD) {
      setActivity(Activity.ATTACKING);
    }
  }
}
