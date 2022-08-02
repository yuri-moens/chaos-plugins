package io.reisub.unethicalite.mining.tasks;

import com.google.common.collect.ImmutableSet;
import io.reisub.unethicalite.mining.Config;
import io.reisub.unethicalite.mining.Location;
import io.reisub.unethicalite.mining.Mining;
import io.reisub.unethicalite.mining.RockPosition;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Setter;
import net.runelite.api.GameState;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.client.Static;

@Singleton
public class Mine extends Task {

  private static final Set<Integer> DROP_IDS =
      ImmutableSet.of(
          ItemID.SANDSTONE_1KG,
          ItemID.SANDSTONE_2KG,
          ItemID.SANDSTONE_5KG,
          ItemID.SANDSTONE_10KG,
          ItemID.GRANITE_500G,
          ItemID.GRANITE_2KG,
          ItemID.GRANITE_5KG,
          ItemID.SODA_ASH);

  private final AtomicInteger ticks = new AtomicInteger(0);
  @Inject private Mining plugin;
  @Inject private Config config;
  private ArrayDeque<RockPosition> rockPositions;
  @Setter
  private RockPosition currentRockPosition;

  @Override
  public String getStatus() {
    return "Mining";
  }

  @Override
  public boolean validate() {
    return (!Inventory.isFull() || config.location() == Location.VOLCANIC_ASH) && isReady();
  }

  @Override
  public int execute() {
    if (!Movement.isRunEnabled()) {
      Movement.toggleRun();
    }

    TileObject rock = getRock();

    if (config.location().isThreeTick()) {
      final Item knife = Inventory.getFirst(ItemID.KNIFE);
      final Item logs = Inventory.getFirst(ItemID.TEAK_LOGS, ItemID.MAHOGANY_LOGS);

      if (knife == null || logs == null) {
        return 1;
      }

      knife.useOn(logs);
    }

    if (rock == null
        && currentRockPosition != null
        && currentRockPosition.getInteractFrom() != null) {
      Movement.walk(currentRockPosition.getInteractFrom());
    }

    while (rock == null && config.location().isThreeTick()) {
      rock =
          TileObjects.getFirstAt(
              currentRockPosition.getRock(), Predicates.ids(config.location().getRockIds()));
    }

    if (rock == null || (Inventory.isFull() && config.location() != Location.VOLCANIC_ASH)) {
      return 1;
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

    return 1;
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

    if (Inventory.isFull() && !config.drop() && config.location().getBankPoint() != null) {
      currentRockPosition = null;
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
      return rocksAvailable()
          && (currentRockPosition == null
          || TileObjects.getFirstAt(
                  currentRockPosition.getRock(), Predicates.ids(config.location().getRockIds()))
              == null);
    }
  }

  private boolean rocksAvailable() {
    final ArrayDeque<RockPosition> rockPositions = config.location().getRockPositions();

    if (rockPositions == null || rockPositions.isEmpty()) {
      return TileObjects.getNearest(Predicates.ids(config.location().getRockIds())) != null;
    } else {
      return TileObjects.getNearest(
          o -> {
            if (!config.location().getRockIds().contains(o.getId())) {
              return false;
            }

            for (RockPosition rockPosition : config.location().getRockPositions()) {
              if (o.getWorldLocation().equals(rockPosition.getRock())) {
                return true;
              }
            }

            return false;
          }) != null;
    }
  }

  private TileObject getRock() {
    if (config.location().getRockPositions() != null && rockPositions == null) {
      resetQueue();
    }

    if (rockPositions == null || rockPositions.isEmpty()) {
      final TileObject rock =
          TileObjects.getNearest(o -> config.location().getRockIds().contains(o.getId()));

      if (rock != null) {
        currentRockPosition = new RockPosition(rock.getWorldLocation(), null);
      }

      return rock;
    } else {
      if (config.location().isThreeTick()) {
        currentRockPosition = rockPositions.poll();
        assert currentRockPosition != null;
        rockPositions.add(currentRockPosition);

        return TileObjects.getFirstAt(
            currentRockPosition.getRock(), Predicates.ids(config.location().getRockIds()));
      } else {
        final TileObject rock = TileObjects.getNearest(
            o -> {
              if (!config.location().getRockIds().contains(o.getId())) {
                return false;
              }

              for (RockPosition rockPosition : config.location().getRockPositions()) {
                if (o.getWorldLocation().equals(rockPosition.getRock())) {
                  return true;
                }
              }

              return false;
            });

        if (rock != null) {
          currentRockPosition = new RockPosition(rock.getWorldLocation(), null);
        }

        return rock;
      }
    }
  }

  private void resetQueue() {
    rockPositions = new ArrayDeque<>();
    rockPositions.addAll(config.location().getRockPositions());
  }

  private boolean shouldDrop() {
    if (config.drop()) {
      return true;
    }

    return config.location() == Location.QUARRY_GRANITE
        || config.location() == Location.VOLCANIC_ASH;
  }
}
