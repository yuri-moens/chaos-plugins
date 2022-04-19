package io.reisub.unethicalite.birdhouse.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.TileItems;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.birdhouse.BirdHouse;
import io.reisub.unethicalite.birdhouse.Config;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;

@RequiredArgsConstructor
public class HarvestSeaweed extends Task {
  private final BirdHouse plugin;
  private final Config config;

  private TileObject seaweed;

  @Override
  public String getStatus() {
    return "Harvesting seaweed";
  }

  @Override
  public boolean validate() {
    return plugin.isUnderwater()
        && Inventory.contains(ItemID.SEED_DIBBER)
        && !Inventory.isFull()
        && ((seaweed = TileObjects.getNearest("Dead seaweed")) != null || (seaweed = TileObjects.getNearest((o) -> o.hasAction("Pick"))) != null);
  }

  @Override
  public void execute() {
    if (seaweed.hasAction("Pick")) {
      int count = TileObjects.getAll((o) -> o.hasAction("Pick")).size();

      GameThread.invoke(() -> seaweed.interact("Pick"));
      Time.sleepTicksUntil(() -> TileObjects.getAll((o) -> o.hasAction("Pick")).size() < count
          || (config.pickupSpores() && TileItems.getNearest(ItemID.SEAWEED_SPORE) != null)
          || Inventory.isFull(), 120);
    } else {
      int count = TileObjects.getAll("Dead seaweed").size();

      GameThread.invoke(() -> seaweed.interact("Clear"));
      Time.sleepTicksUntil(() -> TileObjects.getAll("Dead seaweed").size() < count, 30);
    }
  }
}
