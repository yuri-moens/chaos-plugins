package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;


public class MakeEssence extends Task {
  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Making essence";
  }

  @Override
  public boolean validate() {
    return plugin.getGamePhase() == 10 && !Inventory.isFull()
        &&
        Inventory.contains("Guardian fragments");
  }

  @Override
  public void execute() {
    TileObjects.getNearest("Workbench").interact("Work-at");
    Time.sleepTicksUntil(() -> Inventory.isFull() || !Inventory.contains("Guardian fragments"), 20);
    for (int id : GuardiansOfTheRift.POUCH_IDS) {
      if (Inventory.contains(id)) {
        Inventory.getFirst(id).interact("Fill");
      }
    }
    TileObjects.getNearest("Workbench").interact("Work-at");
    Time.sleepTicksUntil(
        () -> (plugin.isPortalActive() && Inventory.getFreeSlots() >= 10) || !Inventory.contains("Guardian fragments") || Inventory.isFull()
            ||
            !Inventory.contains("Guardian fragments"), 20);
  }
}