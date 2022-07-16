package io.reisub.unethicalite.mining.tasks;

import io.reisub.unethicalite.mining.Config;
import io.reisub.unethicalite.mining.Location;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;

public class CrushBarronite extends Task {

  @Inject
  private Config config;
  @Inject
  private Mine mineTask;

  @Override
  public String getStatus() {
    return "Crushing barronite deposit";
  }

  @Override
  public boolean validate() {
    return config.location() == Location.BARRONITE
        && Inventory.isFull()
        && Inventory.contains(ItemID.BARRONITE_DEPOSIT);
  }

  @Override
  public void execute() {
    mineTask.setCurrentRockPosition(null);
    TileObject crusher = TileObjects.getNearest(ObjectID.BARRONITE_CRUSHER);

    if (crusher == null) {
      final WorldPoint destination = new WorldPoint(2957, 5807, 0);
      Movement.walk(destination);
      Time.sleepTicksUntil(() -> Players.getLocal().distanceTo(destination) < 5, 60);

      crusher = TileObjects.getNearest(ObjectID.BARRONITE_CRUSHER);
    }

    if (crusher == null) {
      return;
    }

    final TileObject finalCrusher = crusher;
    GameThread.invoke(() -> finalCrusher.interact("Smith"));
    if (!Time.sleepTicksUntil(() -> Players.getLocal().isAnimating(), 60)) {
      return;
    }

    Time.sleepTicksUntil(() -> !Inventory.contains(ItemID.BARRONITE_DEPOSIT), 200);
  }
}
