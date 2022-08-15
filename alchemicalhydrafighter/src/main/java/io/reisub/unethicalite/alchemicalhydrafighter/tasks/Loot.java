package io.reisub.unethicalite.alchemicalhydrafighter.tasks;

import io.reisub.unethicalite.alchemicalhydrafighter.AlchemicalHydraFighter;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.TileItem;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileItems;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.client.Static;

public class Loot extends Task {

  @Inject
  private AlchemicalHydraFighter plugin;

  @Override
  public String getStatus() {
    return "Looting";
  }

  @Override
  public boolean validate() {
    return plugin.getPhase() == null
        && Static.getClient().isInInstancedRegion()
        && !TileItems.getAll().isEmpty()
        && !Inventory.isFull();
  }

  @Override
  public void execute() {
    final TileItem loot = TileItems.getNearest(i -> true);

    if (loot == null) {
      return;
    }

    loot.interact("Take");
    Time.sleepTicksUntil(
        () -> TileItems.getFirstAt(loot.getWorldLocation(), loot.getId()) == null, 15);
  }
}
