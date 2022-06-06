package io.reisub.unethicalite.sulliuscep.tasks;

import io.reisub.unethicalite.sulliuscep.Sulliuscep;
import io.reisub.unethicalite.sulliuscep.SulliuscepObject;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.NullObjectID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;

public class FillPit extends Task {

  @Inject
  private Sulliuscep plugin;

  @Override
  public String getStatus() {
    return "Filling pit";
  }

  @Override
  public boolean validate() {
    return !plugin.isPitFilled()
        && plugin.getCurrentSulliuscep() == SulliuscepObject.SULLIUSCEP_1
        && Utils.isInRegion(Sulliuscep.SWAMP_LOWER_REGION_ID)
        && Inventory.getCount(ItemID.MUSHROOM) >= 9;
  }

  @Override
  public void execute() {
    final TileObject pit = TileObjects.getNearest(NullObjectID.NULL_31426);

    if (pit == null) {
      return;
    }

    GameThread.invoke(() -> pit.interact("Fill"));

    Time.sleepTicksUntil(() -> plugin.isPitFilled(), 20);
  }
}
