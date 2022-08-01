package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class MineHugeRemains extends Task {
  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Mining huge remains";
  }

  @Override
  public boolean validate() {
    return !Inventory.isFull() && Players.getLocal().getWorldLocation().getWorldX() < 3597;
  }

  @Override
  public void execute() {
    TileObjects.getNearest("Huge guardian remains").interact("Mine");
    Time.sleepTicksUntil(Inventory::isFull, 20);
    for (int id : GuardiansOfTheRift.POUCH_IDS) {
      if (Inventory.contains(id)) {
        Inventory.getFirst(id).interact("Fill");
      }
    }
    Time.sleepTick();
  }
}