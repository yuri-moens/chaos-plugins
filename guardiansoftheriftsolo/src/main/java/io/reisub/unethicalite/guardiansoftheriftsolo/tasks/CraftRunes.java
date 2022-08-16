package io.reisub.unethicalite.guardiansoftheriftsolo.tasks;

import io.reisub.unethicalite.guardiansoftheriftsolo.GuardiansOfTheRiftSolo;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.AreaType;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class CraftRunes extends Task {

  @Inject
  private GuardiansOfTheRiftSolo plugin;

  @Override
  public String getStatus() {
    return "Crafting runes";
  }

  @Override
  public boolean validate() {
    return AreaType.getCurrent() == AreaType.ALTAR
        && Inventory.contains(ItemID.GUARDIAN_ESSENCE);
  }

  @Override
  public void execute() {
    while (Inventory.contains(ItemID.GUARDIAN_ESSENCE)) {
      craftRunes();

      if (!plugin.arePouchesEmpty()) {
        emptyPouches();
      }
    }
  }

  private void craftRunes() {
    final TileObject altar = TileObjects.getNearest("Altar");

    if (altar == null) {
      return;
    }

    altar.interact("Craft-rune");
    Time.sleepTicksUntil(() -> !Inventory.contains(ItemID.GUARDIAN_ESSENCE), 20);
  }

  private void emptyPouches() {
    for (Item pouch : Inventory.getAll(Predicates.ids(Constants.ESSENCE_POUCH_IDS))) {
      pouch.interact("Empty");
    }

    Time.sleepTicksUntil(() -> Inventory.contains(Predicates.ids(ItemID.GUARDIAN_ESSENCE)), 3);
  }
}
