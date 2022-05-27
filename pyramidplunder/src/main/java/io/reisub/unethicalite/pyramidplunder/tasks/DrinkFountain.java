package io.reisub.unethicalite.pyramidplunder.tasks;

import io.reisub.unethicalite.pyramidplunder.PyramidPlunder;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.magic.SpellBook.Standard;
import net.unethicalite.client.Static;

public class DrinkFountain extends Task {

  @Override
  public String getStatus() {
    return "Drinking from fountain";
  }

  @Override
  public boolean validate() {
    if (Combat.getMissingHealth() == 0 && !Combat.isPoisoned()) {
      return false;
    }

    if (!Standard.TELEPORT_TO_HOUSE.canCast()) {
      return false;
    }

    if (Static.getClient().isInInstancedRegion()
        || Utils.isInRegion(PyramidPlunder.SOPHANEM_REGION)) {
      return true;
    }

    return PyramidPlunder.isInPyramidPlunder()
        && PyramidPlunder.isLastRoom()
        && TileObjects.getNearest(
            o -> o.getName().equals("Grand Gold Chest") && o.hasAction("Search")) == null;
  }

  @Override
  public void execute() {
    if (PyramidPlunder.isInPyramidPlunder() || Utils.isInRegion(PyramidPlunder.SOPHANEM_REGION)) {
      Standard.TELEPORT_TO_HOUSE.cast();

      Time.sleepTicksUntil(
          () -> TileObjects.getNearest(Predicates.ids(Constants.REJUVENATION_POOL_IDS)) != null,
          10);

      Time.sleepTicks(2);
    }

    final TileObject pool = TileObjects.getNearest(Predicates.ids(Constants.REJUVENATION_POOL_IDS));

    if (pool == null) {
      return;
    }

    GameThread.invoke(() -> pool.interact(0));

    Time.sleepTicksUntil(() -> Combat.getMissingHealth() == 0 && !Combat.isPoisoned(), 15);
  }
}
