package io.reisub.unethicalite.birdhouse.tasks;

import io.reisub.unethicalite.birdhouse.BirdHouse;
import io.reisub.unethicalite.birdhouse.BirdHouseSpace;
import io.reisub.unethicalite.birdhouse.BirdHouseState;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.game.Vars;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Reachable;

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
    birdHouse = null;

    for (BirdHouseSpace space : BirdHouseSpace.values()) {
      if (BirdHouseState.fromVarpValue(Vars.getVarp(space.getVarp().getId()))
          == BirdHouseState.SEEDED
          && !plugin.isEmptied(space.getObjectId())) {
        final TileObject tempBirdHouse = TileObjects.getNearest(space.getObjectId());

        if (tempBirdHouse == null) {
          continue;
        }

        if (birdHouse == null
            || Players.getLocal().distanceTo(tempBirdHouse)
            < Players.getLocal().distanceTo(birdHouse)) {
          birdHouse = tempBirdHouse;
        }
      }
    }

    return birdHouse != null && Reachable.isInteractable(birdHouse);
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
