package io.reisub.unethicalite.mining.tasks;

import com.google.common.collect.ImmutableSet;
import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.movement.Movement;
import dev.unethicalite.managers.Static;
import io.reisub.unethicalite.mining.Config;
import io.reisub.unethicalite.mining.Location;
import io.reisub.unethicalite.mining.Mining;
import io.reisub.unethicalite.mining.RockPosition;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import net.runelite.api.GameState;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.eventbus.Subscribe;

public class Mine extends Task {
  private static final Set<Integer> DROP_IDS =
      ImmutableSet.of(
          ItemID.SANDSTONE_1KG,
          ItemID.SANDSTONE_2KG,
          ItemID.SANDSTONE_5KG,
          ItemID.SANDSTONE_10KG,
          ItemID.GRANITE_500G,
          ItemID.GRANITE_2KG,
          ItemID.GRANITE_5KG);
  private final AtomicInteger ticks = new AtomicInteger(0);
  @Inject private Mining plugin;
  @Inject private Config config;
  private LinkedList<RockPosition> rockPositions;
  private RockPosition currentRockPosition;

  @Override
  public String getStatus() {
    return "Mining";
  }

  @Override
  public boolean validate() {
    return !Inventory.isFull() && isReady();
  }

  @Override
  public void execute() {
    if (!Movement.isRunEnabled()) {
      Movement.toggleRun();
    }

    TileObject rock = getRock();

    if (config.location().isThreeTick()) {
      Item knife = Inventory.getFirst(ItemID.KNIFE);
      Item logs = Inventory.getFirst(ItemID.TEAK_LOGS, ItemID.MAHOGANY_LOGS);

      if (knife == null || logs == null) {
        return;
      }

      knife.useOn(logs);
    }

    if (rock == null && currentRockPosition != null) {
      Movement.walk(currentRockPosition.getInteractFrom());
    }

    while (rock == null && config.location().isThreeTick()) {
      rock =
          TileObjects.getFirstAt(
              currentRockPosition.getRock(), Predicates.ids(config.location().getRockIds()));
    }

    if (rock == null || Inventory.isFull()) {
      return;
    }

    rock.interact(0);

    List<Item> dropItems = Inventory.getAll(Predicates.ids(DROP_IDS));
    if (shouldDrop() && !dropItems.isEmpty()) {
      dropItems.forEach(Item::drop);
      rock.interact(0);
    }

    if (!config.location().isThreeTick()) {
      Time.sleepTicksUntil(() -> !Players.getLocal().isIdle(), 10);
    }
  }

  @Subscribe
  private void onGameTick(GameTick event) {
    ticks.incrementAndGet();
  }

  @Subscribe
  private void onItemContainerChanged(ItemContainerChanged event) {
    if (Static.getClient().getGameState() != GameState.LOGGED_IN) {
      return;
    }

    if (Inventory.isFull() && rockPositions != null) {
      resetQueue();
    }
  }

  private boolean isReady() {
    if (config.location().isThreeTick()) {
      if (plugin.isArrived()) {
        ticks.set(0);
        plugin.setArrived(false);
        return true;
      }

      return ticks.get() % 3 == 0;
    } else {
      return Players.getLocal().isIdle();
    }
  }

  private TileObject getRock() {
    if (config.location().getRockPositions() != null && rockPositions == null) {
      resetQueue();
    }

    if (rockPositions == null || rockPositions.isEmpty()) {
      return null;
    } else {
      currentRockPosition = rockPositions.poll();
      rockPositions.add(currentRockPosition);

      return TileObjects.getFirstAt(
          currentRockPosition.getRock(), Predicates.ids(config.location().getRockIds()));
    }
  }

  private void resetQueue() {
    rockPositions = new LinkedList<>();
    rockPositions.addAll(config.location().getRockPositions());
  }

  private boolean shouldDrop() {
    if (config.drop()) {
      return true;
    }

    return config.location() == Location.QUARRY_GRANITE;
  }
}
