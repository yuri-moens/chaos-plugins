package io.reisub.unethicalite.wintertodt.tasks;

import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import io.reisub.unethicalite.wintertodt.Config;
import io.reisub.unethicalite.wintertodt.Side;
import io.reisub.unethicalite.wintertodt.Wintertodt;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;

public class Burn extends Task {

  @Inject
  private Wintertodt plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Feeding brazier";
  }

  @Override
  public boolean validate() {
    if (!plugin.bossIsUp()) {
      return false;
    }

    if (plugin.getPreviousActivity() == Activity.WOODCUTTING
        && !Inventory.isFull()
        && !plugin.shouldStartFeeding()) {
      return false;
    }

    return plugin.getCurrentActivity() == Activity.IDLE
        && Inventory.contains(ItemID.BRUMA_ROOT, ItemID.BRUMA_KINDLING);
  }

  @Override
  public void execute() {
    if (Side.getNearest() == Side.WEST
        && Players.getLocal().getWorldLocation().equals(Side.WEST.getPositionNearBrazier())) {
      Movement.walk(Side.WEST.getPositionNearBrazier());
    }

    final TileObject brazier = TileObjects.getNearest(o -> o.getName().equals("Burning brazier")
        && Players.getLocal().distanceTo(o) < 15);

    if (brazier == null) {
      return;
    }

    GameThread.invoke(() -> brazier.interact("Feed"));

    Time.sleepTicksUntil(() -> plugin.getCurrentActivity() == Activity.FEEDING_BRAZIER
        || TileObjects.getNearest(
            o -> o.getName().equals("Burning brazier")
                && Players.getLocal().distanceTo(o) < 15) == null, 10);

    Time.sleepTick();
  }
}
