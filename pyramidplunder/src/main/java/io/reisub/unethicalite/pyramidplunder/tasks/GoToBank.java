package io.reisub.unethicalite.pyramidplunder.tasks;

import io.reisub.unethicalite.pyramidplunder.PyramidPlunder;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.client.Static;

public class GoToBank extends Task {

  @Override
  public String getStatus() {
    return "Going to bank";
  }

  @Override
  public boolean validate() {
    return (Inventory.getFreeSlots() < 14 || hasTwoSceptres())
        && (Utils.isInRegion(PyramidPlunder.SOPHANEM_REGION)
        || Static.getClient().isInInstancedRegion());
  }

  @Override
  public void execute() {
    if (Static.getClient().isInInstancedRegion()) {
      final Item cape = Inventory.getFirst(Predicates.ids(Constants.CRAFTING_CAPE_IDS));

      if (cape == null) {
        return;
      }

      cape.interact("Teleport");

      Time.sleepTicksUntil(() -> Utils.isInRegion(Constants.CRAFTING_GUILD_REGION), 10);
      Time.sleepTick();

      return;
    }

    final TileObject ladder = TileObjects.getFirstAt(3315, 2797, 0, "Ladder");
    if (ladder == null) {
      return;
    }

    ladder.interact(0);

    if (Static.getClient().getEnergy() < 30
        && Inventory.contains(Predicates.ids(Constants.STAMINA_POTION_IDS))) {
      Time.sleepTick();
      Inventory.getFirst(Predicates.ids(Constants.STAMINA_POTION_IDS)).interact("Drink");
      Time.sleepTick();
      ladder.interact(0);
    }

    Time.sleepTicksUntil(() -> Utils.isInRegion(PyramidPlunder.SOPHANEM_BANK_REGION), 25);
    Time.sleepTick();
  }

  private boolean hasTwoSceptres() {
    if (Inventory.getCount(Predicates.ids(Constants.PHARAOHS_SCEPTRE_IDS)) > 1) {
      return true;
    }

    return Inventory.contains(Predicates.ids(Constants.PHARAOHS_SCEPTRE_IDS))
        && Equipment.contains(Predicates.ids(Constants.PHARAOHS_SCEPTRE_IDS));
  }
}
