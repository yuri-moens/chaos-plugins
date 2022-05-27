package io.reisub.unethicalite.motherlodemine.tasks;

import io.reisub.unethicalite.motherlodemine.Config;
import io.reisub.unethicalite.motherlodemine.MiningArea;
import io.reisub.unethicalite.motherlodemine.MotherlodeMine;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.TileObject;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Reachable;

public class Mine extends Task {

  @Inject
  private MotherlodeMine plugin;
  @Inject
  private Config config;
  private TileObject oreVein;

  @Override
  public String getStatus() {
    return "Mining";
  }

  @Override
  public boolean validate() {
    if (plugin.getMiningArea() == MiningArea.NORTH) {
      if (Players.getLocal().getWorldLocation().getY() < 5684) {
        return false;
      }
    }

    return plugin.getCurrentActivity() == Activity.IDLE
        && !Inventory.isFull()
        && (plugin.isUpstairs() || !config.upstairs())
        && (oreVein = plugin.getMiningArea().getNearestVein()) != null
        && Reachable.isInteractable(oreVein);
  }

  @Override
  public void execute() {
    plugin.setActivity(Activity.MINING);
    GameThread.invoke(() -> oreVein.interact("Mine"));
  }

  @Subscribe
  private void onGameTick(GameTick event) {
    if (plugin.isRunning() && plugin.getCurrentActivity() == Activity.MINING) {
      if (oreVein == null) {
        plugin.setActivity(Activity.IDLE);
      } else {
        final TileObject oreVeinCheck =
            TileObjects.getFirstAt(oreVein.getWorldLocation(), o -> o.hasAction("Mine"));

        if (oreVeinCheck == null) {
          oreVein = null;
          plugin.setActivity(Activity.IDLE);
        }
      }
    }
  }
}
