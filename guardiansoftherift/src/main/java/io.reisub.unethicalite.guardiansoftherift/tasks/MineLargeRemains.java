package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.guardiansoftherift.data.GotrArea;
import io.reisub.unethicalite.guardiansoftherift.data.PluginActivity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class MineLargeRemains extends Task {

  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Mining large remains";
  }

  @Override
  public boolean validate() {
    return plugin.getElapsedTicks() >= 0
        && GotrArea.getCurrent() == GotrArea.LARGE_REMAINS
        && !plugin.isCurrentActivity(PluginActivity.MINING);
  }

  @Override
  public void execute() {
    if (Inventory.isFull()) {
      Inventory.getFirst(ItemID.GUARDIAN_ESSENCE).drop();
      Time.sleepTick();
    }

    TileObjects.getNearest("Large guardian remains").interact("Mine");

    if (!Time.sleepTicksUntil(() -> Players.getLocal().isMoving()
        || Players.getLocal().isAnimating(), 3)) {
      return;
    }

    Time.sleepTicksUntil(() -> plugin.isCurrentActivity(PluginActivity.MINING), 10);
  }
}