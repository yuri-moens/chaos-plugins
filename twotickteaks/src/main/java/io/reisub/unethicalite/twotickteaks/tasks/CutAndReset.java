package io.reisub.unethicalite.twotickteaks.tasks;

import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.TileObject;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;

public class CutAndReset extends Task {

  private Boolean cut = true;

  @Override
  public String getStatus() {
    return "Cutting and Resetting";
  }

  @Override
  public boolean validate() {
    return true;
  }

  @Override
  public int execute() {
    TileObject tree = TileObjects.getNearest("Teak");
    if (cut) {
      if (tree != null && tree.distanceTo(Players.getLocal()) < 2) {
        GameThread.invoke(() -> tree.interact("Chop down"));
      }
      cut = false;
    } else {
      if (Inventory.contains("Teak logs")) {
        Inventory.getFirst("Teak logs").interact("Drop");
      } else {
        if (tree != null && tree.distanceTo(Players.getLocal()) < 2) {
          Movement.walk(tree);
        }
      }
      cut = true;
    }

    return 1;
  }
}
