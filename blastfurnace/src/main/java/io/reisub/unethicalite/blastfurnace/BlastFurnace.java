package io.reisub.unethicalite.blastfurnace;

import com.google.inject.Provides;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.blastfurnace.tasks.DepositMaterials;
import io.reisub.unethicalite.blastfurnace.tasks.HandleBank;
import io.reisub.unethicalite.blastfurnace.tasks.TakeBars;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.enums.Activity;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Blast Furnace",
    description = "So anyway, I started blasting",
    enabledByDefault = false)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class BlastFurnace extends TickScript {
  @Inject private Config config;
  @Getter @Setter private boolean expectingBars;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  protected void onStart() {
    super.onStart();

    addTask(DepositMaterials.class);
    addTask(TakeBars.class);
    addTask(HandleBank.class);
  }

  @Subscribe
  private void onItemContainerChanged(ItemContainerChanged event) {
    final ItemContainer container = event.getItemContainer();

    if (currentActivity == Activity.WITHDRAWING && container.contains(config.metal().getBarId())) {
      setActivity(Activity.IDLE);
    } else if (currentActivity == Activity.DEPOSITING && Inventory.getFreeSlots() >= 27) {
      setActivity(Activity.IDLE);
    }
  }
}
