package io.reisub.unethicalite.smithing;

import com.google.inject.Provides;
import io.reisub.unethicalite.smithing.tasks.HandleBank;
import io.reisub.unethicalite.smithing.tasks.Smith;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Run;
import java.time.Duration;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.AnimationID;
import net.runelite.api.InventoryID;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Inventory;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Smithing",
    description = "I shall make weapons from your bones!",
    enabledByDefault = false)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class Smithing extends TickScript {

  @Inject
  private Config config;
  private int lastBarCount;
  @Getter
  private int itemsMade;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  protected void onStart() {
    super.onStart();

    lastActionTimeout = Duration.ofSeconds(5);
    itemsMade = 0;
    lastBarCount = Inventory.getCount(config.metal().getBarId());

    addTask(Run.class);
    tasks.add(new HandleBank(this, config));
    tasks.add(new Smith(this, config));
  }

  @Subscribe
  private void onItemContainerChanged(ItemContainerChanged event) {
    if (event.getContainerId() != InventoryID.INVENTORY.getId()) {
      return;
    }

    final int count = Inventory.getCount(config.metal().getBarId());

    if (count < lastBarCount) {
      itemsMade++;
    }

    lastBarCount = count;

    if (count < config.product().getRequiredBars()
        && currentActivity == Activity.SMITHING) {
      setActivity(Activity.IDLE);
    }
  }

  @Subscribe
  private void onAnimationChanged(AnimationChanged event) {
    if (!Utils.isLoggedIn()) {
      return;
    }

    switch (Players.getLocal().getAnimation()) {
      case AnimationID.SMITHING_ANVIL:
      case AnimationID.SMITHING_IMCANDO_HAMMER:
        setActivity(Activity.SMITHING);
        break;
      default:
    }
  }
}
