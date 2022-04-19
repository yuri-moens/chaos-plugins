package io.reisub.unethicalite.herblore.tasks;

import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.herblore.Herblore;
import io.reisub.unethicalite.herblore.HerbloreTask;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;

public class ProcessSecondary extends Task {
  @Inject private Herblore plugin;

  @Override
  public String getStatus() {
    return "Processing secondaries";
  }

  @Override
  public boolean validate() {
    return plugin.getConfig().task() == HerbloreTask.PROCESS_SECONDARIES
        && plugin.getCurrentActivity() == Activity.IDLE
        && Inventory.contains(ItemID.PESTLE_AND_MORTAR)
        && Inventory.contains(plugin.getBaseSecondaryIds());
  }

  @Override
  public void execute() {
    plugin.setActivity(Activity.PROCESSING_SECONDARIES);

    List<Item> secondaries = Inventory.getAll(plugin.getBaseSecondaryIds());
    Item pestleAndMortar = Inventory.getFirst(ItemID.PESTLE_AND_MORTAR);

    secondaries.forEach(pestleAndMortar::useOn);
  }
}
