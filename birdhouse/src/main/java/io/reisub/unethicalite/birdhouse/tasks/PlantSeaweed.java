package io.reisub.unethicalite.birdhouse.tasks;

import io.reisub.unethicalite.birdhouse.BirdHouse;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;

@RequiredArgsConstructor
public class PlantSeaweed extends Task {
  private final BirdHouse plugin;

  private TileObject patch;

  @Override
  public String getStatus() {
    return "Planting seaweed";
  }

  @Override
  public boolean validate() {
    return plugin.isUnderwater() && (patch = TileObjects.getNearest("Seaweed patch")) != null;
  }

  @Override
  public void execute() {
    Item spore = Inventory.getFirst(ItemID.SEAWEED_SPORE);
    if (spore == null) {
      return;
    }

    int quantity = spore.getQuantity();

    spore.useOn(patch);
    Time.sleepTicksUntil(() -> Inventory.getCount(true, ItemID.SEAWEED_SPORE) < quantity, 10);
    Time.sleepTicks(3);

    Item compost = Inventory.getFirst(Predicates.ids(Constants.COMPOST_IDS));

    if (compost == null) {
      return;
    }

    patch = TileObjects.getNearest("Seaweed");
    if (patch == null) {
      return;
    }

    GameThread.invoke(() -> compost.useOn(patch));
    Time.sleepTicks(3);
  }
}
