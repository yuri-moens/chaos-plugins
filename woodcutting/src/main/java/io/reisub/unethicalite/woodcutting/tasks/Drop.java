package io.reisub.unethicalite.woodcutting.tasks;

import io.reisub.unethicalite.utils.tasks.Task;
import io.reisub.unethicalite.woodcutting.Config;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Inventory;

public class Drop extends Task {

  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Dropping logs";
  }

  @Override
  public boolean validate() {
    return Inventory.isFull()
        && config.drop();
  }

  @Override
  public void execute() {
    final List<Item> logs = Inventory.getAll(Predicates.nameContains("logs", false));

    if (logs.isEmpty()) {
      return;
    }

    final int ticks = -Math.floorDiv(-logs.size(), 10);

    logs.forEach(Item::drop);

    Time.sleepTicks(ticks);
  }
}
