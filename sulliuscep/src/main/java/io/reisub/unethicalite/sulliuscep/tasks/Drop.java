package io.reisub.unethicalite.sulliuscep.tasks;

import io.reisub.unethicalite.sulliuscep.Sulliuscep;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.client.Static;

public class Drop extends Task {

  @Inject
  private Sulliuscep plugin;

  @Override
  public String getStatus() {
    return "Dropping";
  }

  @Override
  public boolean validate() {
    return Inventory.isFull();
  }

  @Override
  public void execute() {
    Inventory.getAll(ItemID.MORT_MYRE_FUNGUS, ItemID.MUSHROOM)
        .forEach(i -> i.interact("Drop"));

    Time.sleepTicksUntil(() -> !Inventory.isFull(), 3);
    plugin.setLastDrop(Static.getClient().getTickCount());
  }
}
