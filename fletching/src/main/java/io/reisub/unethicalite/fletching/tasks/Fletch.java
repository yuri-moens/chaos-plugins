package io.reisub.unethicalite.fletching.tasks;

import io.reisub.unethicalite.fletching.Config;
import io.reisub.unethicalite.fletching.Fletching;
import io.reisub.unethicalite.fletching.data.PluginActivity;
import io.reisub.unethicalite.utils.api.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Dialog;
import net.unethicalite.api.widgets.Production;

public class Fletch extends Task {

  @Inject
  private Fletching plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Fletching";
  }

  @Override
  public boolean validate() {
    return plugin.isCurrentActivity(Activity.IDLE)
        && Inventory.contains(ItemID.KNIFE)
        && Inventory.contains(config.logType().getId());
  }

  @Override
  public void execute() {
    if (Dialog.isOpen()) {
      Dialog.close();
    }

    final Item knife = Inventory.getFirst(ItemID.KNIFE);
    final Item log = Inventory.getFirst(config.logType().getId());

    knife.useOn(log);
    Time.sleepTicksUntil(Production::isOpen, 3);

    Production.chooseOption(config.product().getProductionIndex());

    if (Time.sleepTicksUntil(() -> Players.getLocal().isAnimating(), 5)) {
      plugin.setActivity(PluginActivity.FLETCHING);
    }
  }
}
