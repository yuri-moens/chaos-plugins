package io.reisub.unethicalite.secondarygatherer.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileItems;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.movement.Movement;
import io.reisub.unethicalite.secondarygatherer.Config;
import io.reisub.unethicalite.secondarygatherer.Secondary;
import io.reisub.unethicalite.secondarygatherer.SecondaryGatherer;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.ArrayDeque;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.TileItem;
import net.runelite.api.coords.WorldPoint;

public class TakeScale extends Task {

  @Inject
  private SecondaryGatherer plugin;
  @Inject
  private Config config;

  private final ArrayDeque<WorldPoint> scaleLocations = new ArrayDeque<>(4);

  @Override
  public String getStatus() {
    return "Taking scale";
  }

  @Override
  public boolean validate() {
    return config.secondary() == Secondary.BLUE_DRAGON_SCALE
        && !Inventory.isFull()
        && Players.getLocal().distanceTo(GoToScales.SCALES_WORLDPOINT) < 15;
  }

  @Override
  public void execute() {
    if (!Inventory.contains(ItemID.BLUE_DRAGON_SCALE) || scaleLocations.isEmpty()) {
      resetQueue();
    }

    if (!plugin.getCombatHelper().getPrayerHelper().isFlicking()) {
      plugin.getCombatHelper().getPrayerHelper().toggleFlicking();
    }

    if (Inventory.contains(config.food())) {
      Inventory.getFirst(config.food()).interact("Eat");
    }

    final WorldPoint scaleLocation = scaleLocations.poll();
    assert scaleLocation != null;
    scaleLocations.addLast(scaleLocation);

    TileItem scale = TileItems.getFirstAt(scaleLocation, ItemID.BLUE_DRAGON_SCALE);
    if (scale == null) {
      Movement.walk(scaleLocation);

      Time.sleepTicksUntil(
          () -> TileItems.getFirstAt(scaleLocation, ItemID.BLUE_DRAGON_SCALE) != null, 20);
      scale = TileItems.getFirstAt(scaleLocation, ItemID.BLUE_DRAGON_SCALE);
    }

    if (scale == null) {
      return;
    }

    final int scaleCount = Inventory.getCount(ItemID.BLUE_DRAGON_SCALE);
    scale.interact("Take");

    Time.sleepTicksUntil(() -> Inventory.getCount(ItemID.BLUE_DRAGON_SCALE) > scaleCount, 15);
  }

  private void resetQueue() {
    scaleLocations.clear();
    scaleLocations.add(new WorldPoint(1923, 8970, 1));
    scaleLocations.add(new WorldPoint(1922, 8967, 1));
    scaleLocations.add(new WorldPoint(1928, 8964, 1));
    scaleLocations.add(new WorldPoint(1932, 8965, 1));
  }
}
