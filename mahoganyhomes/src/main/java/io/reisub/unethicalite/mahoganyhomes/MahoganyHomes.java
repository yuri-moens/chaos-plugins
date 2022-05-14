package io.reisub.unethicalite.mahoganyhomes;

import com.google.inject.Provides;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.client.Static;
import io.reisub.unethicalite.mahoganyhomes.tasks.Fix;
import io.reisub.unethicalite.mahoganyhomes.tasks.GetTask;
import io.reisub.unethicalite.mahoganyhomes.tasks.GoToHome;
import io.reisub.unethicalite.mahoganyhomes.tasks.HandleBank;
import io.reisub.unethicalite.mahoganyhomes.tasks.TalkToNpc;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Mahogany Homes",
    description = "Can we build it? Yes, we can!",
    enabledByDefault = false)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class MahoganyHomes extends TickScript {

  @Inject
  private Config config;
  @Getter
  @Setter
  private Home previousHome;
  @Getter
  @Setter
  private Home currentHome;
  @Getter
  private PlankSack plankSack;
  @Getter
  @Setter
  private int lastStairsUsed;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  protected void onStart() {
    super.onStart();

    currentHome = null;
    plankSack = injector.getInstance(PlankSack.class);
    Static.getEventBus().register(plankSack);

    if (Inventory.contains(ItemID.PLANK_SACK)) {
      Inventory.getFirst(ItemID.PLANK_SACK).interact("Check");
    }

    addTask(GetTask.class);
    addTask(HandleBank.class);
    addTask(GoToHome.class);
    addTask(Fix.class);
    addTask(TalkToNpc.class);
  }

  @Override
  protected void onStop() {
    super.onStop();

    Static.getEventBus().unregister(plankSack);
    plankSack = null;
    currentHome = null;
  }
}
