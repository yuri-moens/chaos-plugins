package io.reisub.unethicalite.twotickteaks.tasks;

import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.TileObject;

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
  public void execute() {
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
          ChaosMovement.sendMovementPacket(tree.getWorldLocation());
        }
      }
      cut = true;
    }
  }
}
