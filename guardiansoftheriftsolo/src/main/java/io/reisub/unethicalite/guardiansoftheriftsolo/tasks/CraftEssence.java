package io.reisub.unethicalite.guardiansoftheriftsolo.tasks;

import io.reisub.unethicalite.guardiansoftheriftsolo.GuardiansOfTheRiftSolo;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.AreaType;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.CellType;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.GuardianInfo;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class CraftEssence extends Task {

  @Inject
  private GuardiansOfTheRiftSolo plugin;

  @Override
  public String getStatus() {
    return "Crafting essence";
  }

  @Override
  public boolean validate() {
    if (plugin.getElapsedTicks() > 115 / 0.6 && plugin.getElapsedTicks() < 125 / 0.6) {
      return false;
    }

    return plugin.isGameActive()
        && AreaType.getCurrent() == AreaType.MAIN
        && Inventory.contains(ItemID.GUARDIAN_FRAGMENTS)
        && (!Inventory.isFull() || !plugin.arePouchesFull());
  }

  @Override
  public void execute() {
    if (Inventory.contains(ItemID.GUARDIAN_ESSENCE) && !plugin.arePouchesFull()) {
      for (Item pouch : Inventory.getAll(Predicates.ids(Constants.ESSENCE_POUCH_IDS))) {
        pouch.interact("Fill");
      }
    }

    final TileObject workbench = TileObjects.getNearest(ObjectID.WORKBENCH_43754);

    if (workbench == null) {
      return;
    }

    workbench.interact("Work-at");
    if (!Time.sleepTicksUntil(() -> Players.getLocal().getAnimation() == 9365, 20)) {
      return;
    }

    while (!Inventory.isFull()
        && (plugin.getElapsedTicks() < 115 / 0.6 || plugin.getElapsedTicks() > 125 / 0.6)
        && (GuardianInfo.getBest().getCellType() != CellType.OVERCHARGED
        || plugin.getEntranceTimer() > 8)) {
      craft();

      if (!plugin.arePouchesFull() && Inventory.isFull()) {
        fillPouches();
      }
    }
  }

  private void craft() {
    final TileObject workbench = TileObjects.getNearest(ObjectID.WORKBENCH_43754);

    if (workbench == null) {
      return;
    }

    workbench.interact("Work-at");
    Time.sleepTicksUntil(() -> Inventory.isFull()
        || (plugin.getElapsedTicks() > 115 / 0.6 && plugin.getElapsedTicks() < 125 / 0.6)
        || (GuardianInfo.getBest().getCellType() == CellType.OVERCHARGED
            && plugin.getEntranceTimer() <= 8),
        50);
  }

  private void fillPouches() {
    for (Item pouch : Inventory.getAll(Predicates.ids(Constants.ESSENCE_POUCH_IDS))) {
      pouch.interact("Fill");
    }

    Time.sleepTicksUntil(() -> !Inventory.isFull()
        || plugin.arePouchesFull(), 3);
  }
}
