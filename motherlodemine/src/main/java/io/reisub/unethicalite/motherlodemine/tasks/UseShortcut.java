package io.reisub.unethicalite.motherlodemine.tasks;

import io.reisub.unethicalite.motherlodemine.Config;
import io.reisub.unethicalite.motherlodemine.MotherlodeMine;
import io.reisub.unethicalite.utils.enums.Activity;
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

public class UseShortcut extends Task {

  private static final int X_BETWEEN_TUNNEL_ENTRANCES = 3761;

  @Inject
  private MotherlodeMine plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Using shortcut";
  }

  @Override
  public boolean validate() {
    if (config.upstairs() || !config.shortcut() || plugin.getCurrentActivity() != Activity.IDLE) {
      return false;
    }

    if (!Inventory.contains(ItemID.PAYDIRT)
        && Players.getLocal().getWorldLocation().getX() < X_BETWEEN_TUNNEL_ENTRANCES) {
      return true;
    }

    return Inventory.isFull()
        && Players.getLocal().getWorldLocation().getX() > X_BETWEEN_TUNNEL_ENTRANCES;
  }

  @Override
  public void execute() {
    final TileObject tunnel = TileObjects.getNearest(ObjectID.DARK_TUNNEL_10047);

    if (tunnel == null) {
      return;
    }

    final WorldPoint destination =
        Players.getLocal().getWorldLocation().getX() > X_BETWEEN_TUNNEL_ENTRANCES
            ? new WorldPoint(3759, 5670, 0)
            : new WorldPoint(3765, 5671, 0);

    if (Players.getLocal().getWorldLocation().getX() > X_BETWEEN_TUNNEL_ENTRANCES) {
      plugin.mineRockfall(3766, 5670);
    }

    GameThread.invoke(() -> tunnel.interact("Enter"));
    Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().equals(destination), 20);
    Time.sleepTick();
  }
}
