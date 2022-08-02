package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;


public class CraftRunes extends Task {
  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Crafting Runes";
  }

  @Override
  public boolean validate() {
    return TileObjects.getNearest("Altar") != null;
  }

  @Override
  public int execute() {
    Time.sleepTick();
    TileObjects.getNearest("Altar").interact("Craft-rune");
    Time.sleepTicksUntil(() -> !Inventory.contains("Guardian essence"), 15);
    for (int id : plugin.POUCH_IDS) {
      if (Inventory.contains(id)) {
        Inventory.getFirst(id).interact("Empty");
      }
    }
    Time.sleepTick();
    TileObjects.getNearest("Altar").interact("Craft-rune");
    Time.sleepTicksUntil(() -> !Inventory.contains("Guardian essence"), 15);

    return 1;
  }
}