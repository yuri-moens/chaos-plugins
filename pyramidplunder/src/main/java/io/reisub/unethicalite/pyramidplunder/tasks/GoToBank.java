package io.reisub.unethicalite.pyramidplunder.tasks;

import dev.unethicalite.api.commons.Predicates;
import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.client.Static;
import io.reisub.unethicalite.pyramidplunder.PyramidPlunder;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.TileObject;

public class GoToBank extends Task {

  @Override
  public String getStatus() {
    return "Going to bank";
  }

  @Override
  public boolean validate() {
    return Inventory.getFreeSlots() < 14
        && Utils.isInRegion(PyramidPlunder.SOPHANEM_REGION);
  }

  @Override
  public void execute() {

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
}
