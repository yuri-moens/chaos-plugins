package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.guardiansoftherift.data.GotrArea;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class CraftRunes extends Task {

  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Crafting runes";
  }

  @Override
  public boolean validate() {
    return GotrArea.getCurrent() == GotrArea.ALTAR
        && Inventory.contains(ItemID.GUARDIAN_ESSENCE);
  }

  @Override
  public void execute() {
    while (Inventory.contains(ItemID.GUARDIAN_ESSENCE)) {
      boolean success = craftRunes();

      if ((!plugin.arePouchesEmpty() || plugin.getGuardianPower() >= config.guardianPowerLastRun())
          && success) {
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

    return Time.sleepTicksUntil(() -> !Inventory.contains(ItemID.GUARDIAN_ESSENCE), 20);
  }

  private boolean emptyPouches() {
    for (Item pouch : Inventory.getAll(Predicates.ids(Constants.ESSENCE_POUCH_IDS))) {
      pouch.interact("Empty");
    }

    Time.sleepTicksUntil(() -> Inventory.contains(Predicates.ids(ItemID.GUARDIAN_ESSENCE)), 3);

    return Inventory.getFreeSlots() > 0;
  }
}