package io.reisub.unethicalite.motherlodemine.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.movement.Reachable;
import io.reisub.unethicalite.motherlodemine.Config;
import io.reisub.unethicalite.motherlodemine.MiningArea;
import io.reisub.unethicalite.motherlodemine.MotherlodeMine;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.TileObject;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;

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
    GameThread.invoke(() -> oreVein.interact("Mine"));
    Time.sleepTicksUntil(() -> plugin.getCurrentActivity() == Activity.MINING, 15);
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
