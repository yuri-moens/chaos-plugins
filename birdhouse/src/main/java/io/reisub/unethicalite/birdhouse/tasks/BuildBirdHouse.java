package io.reisub.unethicalite.birdhouse.tasks;

import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;

public class BuildBirdHouse extends Task {
  @Override
  public String getStatus() {
    return "Building bird house";
  }

  @Override
  public boolean validate() {
    return Inventory.contains((i) -> Constants.BIRD_HOUSE_ITEM_IDS.contains(i.getId()));
  }

  @Override
  public void execute() {
    TileObject space =
        TileObjects.getNearest(
            (o) ->
                Constants.BIRD_HOUSE_SPACES.contains(o.getId())
                    && o.getTransformedComposition().getImpostor() != null
                    && o.getTransformedComposition().getImpostor().getId()
                        == Constants.BIRD_HOUSE_EMPTY_SPACE);

    if (space == null) {
      return;
    }

    GameThread.invoke(() -> space.interact(0));
    Time.sleepTicksUntil(
        () -> !Inventory.contains((i) -> Constants.BIRD_HOUSE_ITEM_IDS.contains(i.getId())), 5);
  }
}
