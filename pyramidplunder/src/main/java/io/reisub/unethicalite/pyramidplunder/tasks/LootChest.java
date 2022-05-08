package io.reisub.unethicalite.pyramidplunder.tasks;

import dev.unethicalite.api.commons.Predicates;
import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.pyramidplunder.PyramidPlunder;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.TileObject;

public class LootChest extends Task {

  private TileObject chest;

  @Override
  public String getStatus() {
    return "Looting chest";
  }

  @Override
  public boolean validate() {
    if (!PyramidPlunder.isInPyramidPlunder()) {
      return false;
    }

    chest = TileObjects.getNearest(
        o -> o.getName().equals("Grand Gold Chest") && o.hasAction("Search"));

    return chest != null;
  }

  @Override
  public void execute() {
    if (Inventory.isFull()) {
      final Item stoneItem = Inventory.getFirst(Predicates.nameContains("Stone"));
      if (stoneItem != null) {
        stoneItem.interact("Drop");
        Time.sleepTick();
      }
    }

    GameThread.invoke(() -> chest.interact(0));
    Time.sleepTicksUntil(() -> TileObjects.getNearest(
        o -> o.getName().equals("Grand Gold Chest") && o.hasAction("Search")) == null, 15);
  }
}
