package io.reisub.unethicalite.birdhouse.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.TileObject;

public class AddSeeds extends Task {
  private TileObject emptyBirdhouse;

  @Override
  public String getStatus() {
    return "Adding seeds";
  }

  @Override
  public boolean validate() {
    emptyBirdhouse = TileObjects.getNearest(
        (o) -> Constants.BIRD_HOUSE_SPACES.contains(o.getId())
            && o.getTransformedComposition().getImpostor() != null
            && Constants.BIRD_HOUSE_EMPTY_IDS.contains(
                o.getTransformedComposition().getImpostor().getId())
    );

    return emptyBirdhouse != null;
  }

  @Override
  public void execute() {
    Item seeds = Inventory.getFirst((i) -> Constants.BIRD_HOUSE_SEED_IDS.contains(i.getId()));
    if (seeds == null) {
      return;
    }

    int quantity = seeds.getQuantity();
    seeds.useOn(emptyBirdhouse);

    Time.sleepTicksUntil(
        () -> Inventory.getCount(
            true, (i) -> Constants.BIRD_HOUSE_SEED_IDS.contains(i.getId())) < quantity, 5);
  }
}
