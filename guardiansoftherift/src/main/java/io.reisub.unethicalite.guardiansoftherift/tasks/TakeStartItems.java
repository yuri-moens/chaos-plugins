package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.guardiansoftherift.data.GotrArea;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class TakeStartItems extends Task {

  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Taking start items";
  }

  @Override
  public boolean validate() {
    return (plugin.getElapsedTicks() == -1
        || (plugin.getElapsedTicks() < 100 && Inventory.contains(ItemID.GUARDIAN_ESSENCE)))
        && GotrArea.getCurrent() == GotrArea.MAIN
        && (!Inventory.contains("Weak cell")
        || Inventory.getCount(true, "Uncharged cell") < 10);
  }

  @Override
  public void execute() {
    if (!Inventory.contains("Weak cell")) {
      if (Inventory.isFull()) {
        Inventory.getFirst(ItemID.GUARDIAN_ESSENCE).drop();;
      }

      TileObjects.getNearest("Weak cells").interact("Take");
      Time.sleepTicksUntil(() -> Inventory.contains("Weak cell"), 20);
    }

    if (Inventory.getCount(true, "Uncharged cell") < 10) {
      if (Inventory.isFull() && !Inventory.contains("Uncharged cell")) {
        Inventory.getFirst(ItemID.GUARDIAN_ESSENCE).drop();;
      }

      TileObjects.getNearest("Uncharged cells").interact("Take-10");
      Time.sleepTicksUntil(() -> Inventory.getCount(true, "Uncharged cell") == 10, 20);
    }
  }
}