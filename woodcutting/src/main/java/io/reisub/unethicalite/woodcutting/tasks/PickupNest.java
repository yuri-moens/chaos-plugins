package io.reisub.unethicalite.woodcutting.tasks;

import io.reisub.unethicalite.utils.tasks.Task;
import io.reisub.unethicalite.woodcutting.Config;
import javax.inject.Inject;
import net.runelite.api.TileItem;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileItems;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;

public class PickupNest extends Task {

  @Inject
  private Config config;
  @Inject
  private Chop chopTask;

  @Override
  public String getStatus() {
    return "Picking up bird's nest";
  }

  @Override
  public boolean validate() {
    return config.birdNests()
        && !Inventory.isFull()
        && TileItems.getNearest(Predicates.nameContains("nest", false)) != null;
  }

  @Override
  public void execute() {
    final TileItem nest = TileItems.getNearest(Predicates.nameContains("nest", false));

    if (nest == null) {
      return;
    }

    GameThread.invoke(() -> nest.interact("Take"));

    Time.sleepTicksUntil(
        () -> TileItems.getNearest(Predicates.nameContains("nest", false)) == null, 20);

    final TileObject tree = TileObjects.getFirstAt(
        chopTask.getCurrentTreePosition()
            .dx(config.location().getXoffset())
            .dy(config.location().getYoffset()),
        Predicates.ids(config.location().getTreeIds())
    );

    if (tree == null) {
      chopTask.setCurrentTreePosition(null);
      return;
    }

    GameThread.invoke(() -> tree.interact("Chop down"));
  }
}
