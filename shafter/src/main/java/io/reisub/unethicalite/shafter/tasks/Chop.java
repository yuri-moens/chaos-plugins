package io.reisub.unethicalite.shafter.tasks;

import io.reisub.unethicalite.shafter.Config;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;

public class Chop extends Task {

  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Chopping";
  }

  @Override
  public boolean validate() {
    return !Inventory.isFull()
        && Players.getLocal().isIdle();
  }

  @Override
  public void execute() {
    final TileObject tree = TileObjects.getNearest(config.type().getName());

    if (tree == null) {
      return;
    }

    GameThread.invoke(() -> tree.interact("Chop down"));
    Time.sleepTicksUntil(() -> Players.getLocal().isAnimating(), 15);
  }
}
