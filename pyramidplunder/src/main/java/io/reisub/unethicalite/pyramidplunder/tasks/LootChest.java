package io.reisub.unethicalite.pyramidplunder.tasks;

import io.reisub.unethicalite.pyramidplunder.PyramidPlunder;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Reachable;

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

    return chest != null && Reachable.isInteractable(chest);
  }

  @Override
  public int execute() {
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

    return 1;
  }
}
