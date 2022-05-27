package io.reisub.unethicalite.zmi.tasks;

import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import io.reisub.unethicalite.zmi.Config;
import io.reisub.unethicalite.zmi.Zmi;
import javax.inject.Inject;
import net.runelite.api.ObjectID;
import net.runelite.api.Prayer;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Prayers;
import net.unethicalite.client.Static;

public class RuneCraft extends Task {
  @Inject private Config config;

  private int last;

  @Override
  public String getStatus() {
    return "Crafting runes";
  }

  @Override
  public boolean validate() {
    return Inventory.contains(Predicates.ids(Constants.ESSENCE_IDS))
        && Static.getClient().getTickCount() >= last + 3
        && (!Players.getLocal().isMoving()
            || (Players.getLocal().getWorldLocation().equals(Zmi.NEAR_ALTAR))
                && Static.getClient().getTickCount() < Zmi.lastEmpty + 10);
  }

  @Override
  public void execute() {
    last = Static.getClient().getTickCount();
    if (!Inventory.isFull()) {
      Zmi.pouchesAreEmpty = true;
    }

    TileObject altar = TileObjects.getNearest(ObjectID.ALTAR_29631);
    if (altar == null) {
      return;
    }

    altar.interact("Craft-rune");
    if (Players.getLocal().distanceTo(altar) > 5) {
      Time.sleepTicksUntil(() -> Players.getLocal().isMoving(), 5);

      if (config.usePrayer()) {
        if (!Prayers.isEnabled(Prayer.RAPID_HEAL)) {
          Prayers.toggle(Prayer.RAPID_HEAL);
        }

        if (!Prayers.isEnabled(Prayer.PIETY)) {
          Prayers.toggle(Prayer.PIETY);
        }
      }
    }
  }
}
