package io.reisub.unethicalite.daeyaltessence.tasks;

import io.reisub.unethicalite.daeyaltessence.DaeyaltEssence;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class GoToMine extends Task {
  private static final WorldPoint ESSENCE_MINE_LADDER = new WorldPoint(3635, 3340, 0);

  @Override
  public String getStatus() {
    return "Going to essence mine";
  }

  @Override
  public boolean validate() {
    return Players.getLocal().getWorldLocation().getRegionID()
        != DaeyaltEssence.ESSENCE_MINE_REGION;
  }

  @Override
  public int execute() {
    ChaosMovement.walkTo(ESSENCE_MINE_LADDER, 1);

    TileObject ladder = TileObjects.getNearest(ObjectID.STAIRCASE_39092);
    if (ladder == null) {
      return 1;
    }

    ladder.interact(0);
    Time.sleepTicksUntil(
        () ->
            Players.getLocal().getWorldLocation().getRegionID()
                == DaeyaltEssence.ESSENCE_MINE_REGION,
        20);

    Time.sleepTicks(2);

    Inventory.getAll((i) -> i.getName().contains("Graceful")).forEach((i) -> i.interact("Wear"));

    return 1;
  }
}
