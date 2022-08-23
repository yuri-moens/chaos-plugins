package io.reisub.unethicalite.runecrafting.tasks;

import io.reisub.unethicalite.runecrafting.Config;
import io.reisub.unethicalite.runecrafting.Runecrafting;
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

public class CraftRunes extends Task {

  @Inject
  private Runecrafting plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Crafting runes";
  }

  @Override
  public boolean validate() {
    return Inventory.contains(Predicates.ids(Constants.ESSENCE_IDS))
        && TileObjects.getNearest("Altar") != null;
  }

  @Override
  public void execute() {
    while (Inventory.contains(Predicates.ids(Constants.ESSENCE_IDS))) {
      boolean success = craftRunes();

      if (!plugin.arePouchesEmpty() && success) {
        if (emptyPouches()) {
          plugin.setEmptyPouches(4);
          plugin.setFullPouches(0);
        }
      }
    }
  }

  private boolean craftRunes() {
    final TileObject altar = TileObjects.getNearest("Altar");

    if (altar == null) {
      return false;
    }

    altar.interact("Craft-rune");

    if (!Time.sleepTicksUntil(() -> Players.getLocal().isMoving()
        || Players.getLocal().isAnimating(), 2)) {
      return false;
    }

    return Time.sleepTicksUntil(
        () -> !Inventory.contains(Predicates.ids(Constants.ESSENCE_IDS)), 20);
  }

  private boolean emptyPouches() {
    for (Item pouch : Inventory.getAll(Predicates.ids(Constants.ESSENCE_POUCH_IDS))) {
      pouch.interact("Empty");
    }

    Time.sleepTicksUntil(
        () -> Inventory.contains(Predicates.ids(Constants.ESSENCE_IDS)), 3);

    return Inventory.getFreeSlots() > 0;
  }
}
