package io.reisub.unethicalite.birdhouse.tasks;

import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;

public class AddSeeds extends Task {
  private TileObject emptyBirdhouse;

  @Override
  public String getStatus() {
    return "Adding seeds";
  }

  @Override
  public boolean validate() {
    emptyBirdhouse =
        TileObjects.getNearest(
            (o) ->
                Constants.BIRD_HOUSE_SPACES.contains(o.getId())
                    && o.getTransformedComposition().getImpostor() != null
                    && Constants.BIRD_HOUSE_EMPTY_IDS.contains(
                        o.getTransformedComposition().getImpostor().getId()));

    return emptyBirdhouse != null;
  }

  @Override
  public void execute() {
    final Item seeds = Inventory.getFirst((i) -> Constants.BIRD_HOUSE_SEED_IDS.contains(i.getId()));
    if (seeds == null) {
      return;
    }

    final int quantity = seeds.getQuantity();
    GameThread.invoke(() -> seeds.useOn(emptyBirdhouse));

    Time.sleepTicksUntil(
        () ->
            Inventory.getCount(true, (i) -> Constants.BIRD_HOUSE_SEED_IDS.contains(i.getId()))
                < quantity,
        5);
  }
}
