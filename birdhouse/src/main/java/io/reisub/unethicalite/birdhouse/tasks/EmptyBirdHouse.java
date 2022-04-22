package io.reisub.unethicalite.birdhouse.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.birdhouse.BirdHouse;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;

@RequiredArgsConstructor
public class EmptyBirdHouse extends Task {
  private final BirdHouse plugin;

  private TileObject birdHouse;

  @Override
  public String getStatus() {
    return "Emptying bird house";
  }

  @Override
  public boolean validate() {
    birdHouse =
        TileObjects.getNearest(
            (o) ->
                Constants.BIRD_HOUSE_SPACES.contains(o.getId())
                    && !plugin.isEmptied(o.getId())
                    && o.getTransformedComposition().getImpostor() != null
                    && Constants.BIRD_HOUSE_IDS.contains(
                        o.getTransformedComposition().getImpostor().getId()));

	if (birdHouse.getId() == Constants.VERDANT_SOUTH_SPACE) {
		plugin.emptied(birdHouse.getId());
		return false;
	}

    return birdHouse != null;
  }

  @Override
  public void execute() {
    GameThread.invoke(() -> birdHouse.interact(2));
    if (!Time.sleepTicksUntil(() -> Inventory.contains(ItemID.CLOCKWORK), 15)) {
      return;
    }

    plugin.emptied(birdHouse.getId());
  }
}
