package io.reisub.unethicalite.guardiansoftheriftsolo.tasks;

import io.reisub.unethicalite.guardiansoftheriftsolo.GuardiansOfTheRiftSolo;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.AreaType;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class MineHugeRemains extends Task {

  @Inject
  private GuardiansOfTheRiftSolo plugin;

  @Override
  public String getStatus() {
    return "Mining huge remains";
  }

  @Override
  public boolean validate() {
    return plugin.isGameActive()
        && AreaType.getCurrent() == AreaType.HUGE_REMAINS
        && !Inventory.isFull();
  }

  @Override
  public void execute() {
    while (!Inventory.isFull()) {
      mine();

      if (!plugin.arePouchesFull()) {
        fillPouches();
      }
    }
  }

  private void mine() {
    final TileObject remains = TileObjects.getNearest(ObjectID.HUGE_GUARDIAN_REMAINS);

    if (remains == null) {
      return;
    }

    remains.interact("Mine");
    Time.sleepTicksUntil(Inventory::isFull, 20);
  }

  private void fillPouches() {
    for (Item pouch : Inventory.getAll(Predicates.ids(Constants.ESSENCE_POUCH_IDS))) {
      pouch.interact("Fill");
    }

    Time.sleepTicksUntil(() -> !Inventory.isFull()
        || plugin.arePouchesFull(), 3);
  }
}
