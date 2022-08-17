package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.guardiansoftherift.data.GotrArea;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Predicates;
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
    return !Inventory.isFull()
        && GotrArea.getCurrent() == GotrArea.HUGE_REMAINS;
  }

  @Override
  public void execute() {
    while (!Inventory.isFull()) {
      mine();

      if (!plugin.arePouchesFull()) {
        fillPouches();
      }
    }

    Time.sleepTick();
  }

  private void mine() {
    final TileObject remains = TileObjects.getNearest("Huge guardian remains");

    if (remains == null) {
      return;
    }

    remains.interact("Mine");

    if (!Time.sleepTicksUntil(() -> Players.getLocal().isMoving()
        || Players.getLocal().isAnimating(), 3)) {
      return;
    }

    Time.sleepTicksUntil(Inventory::isFull, 20);
  }

  private void fillPouches() {
    for (Item pouch : Inventory.getAll(Predicates.ids(Constants.ESSENCE_POUCH_IDS))) {
      pouch.interact("Fill");
    }

    Time.sleepTicksUntil(() -> Inventory.getFreeSlots() > 0 || plugin.arePouchesFull(), 3);
  }
}