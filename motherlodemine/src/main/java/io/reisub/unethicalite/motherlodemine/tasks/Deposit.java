package io.reisub.unethicalite.motherlodemine.tasks;

import io.reisub.unethicalite.motherlodemine.MotherlodeMine;
import io.reisub.unethicalite.utils.api.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Rand;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;

public class Deposit extends Task {

  private static final int X_BETWEEN_TUNNEL_ENTRANCES = 3761;

  @Inject
  private MotherlodeMine plugin;

  @Override
  public String getStatus() {
    return "Depositing in hopper";
  }

  @Override
  public boolean validate() {
    return plugin.isCurrentActivity(Activity.IDLE)
        && !plugin.isUpstairs()
        && Inventory.contains(ItemID.PAYDIRT)
        && Inventory.isFull()
        && Players.getLocal().getWorldLocation().getX() < X_BETWEEN_TUNNEL_ENTRANCES;
  }

  @Override
  public void execute() {
    plugin.setActivity(Activity.DEPOSITING);

    if (Players.getLocal().getWorldLocation().getY() >= 5680) {
      plugin.mineRockfall(3731, 5683);
      plugin.mineRockfall(3733, 5680);

      Movement.walk(new WorldPoint(3746, 5674, 0).dx(Rand.nextInt(-1, 2)).dy(Rand.nextInt(-1, 2)));

      Time.sleepTicksUntil(() -> Players.getLocal().isMoving(), 3);
      Time.sleepTicksUntil(() -> !Players.getLocal().isMoving()
          || Players.getLocal().getWorldLocation().getY() < 5680, 30);
    }

    final TileObject hopper = TileObjects.getNearest("Hopper");

    GameThread.invoke(() -> hopper.interact("Deposit"));
    Time.sleepTicksUntil(() -> plugin.isCurrentActivity(Activity.IDLE), 15);
    Time.sleepTicks(2);
  }
}
