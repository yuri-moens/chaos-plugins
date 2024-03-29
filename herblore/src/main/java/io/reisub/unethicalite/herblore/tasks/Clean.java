package io.reisub.unethicalite.herblore.tasks;

import io.reisub.unethicalite.herblore.Herblore;
import io.reisub.unethicalite.herblore.HerbloreTask;
import io.reisub.unethicalite.herblore.data.PluginActivity;
import io.reisub.unethicalite.utils.api.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.unethicalite.api.items.Inventory;

public class Clean extends Task {
  @Inject private Herblore plugin;

  @Override
  public String getStatus() {
    return "Cleaning herbs";
  }

  @Override
  public boolean validate() {
    HerbloreTask task = plugin.getConfig().task();

    return (task == HerbloreTask.CLEAN_HERBS
            || task == HerbloreTask.MAKE_UNFINISHED
            || task == HerbloreTask.MAKE_POTION
            || task == HerbloreTask.TAR_HERBS)
        && plugin.isCurrentActivity(Activity.IDLE)
        && Inventory.contains(plugin.getGrimyHerbIds());
  }

  @Override
  public void execute() {
    plugin.setActivity(PluginActivity.CLEANING_HERBS);
    Inventory.getAll(plugin.getGrimyHerbIds()).forEach((i) -> i.interact("Clean"));
  }
}
