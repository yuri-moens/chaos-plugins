package io.reisub.unethicalite.woodcutting.tasks;

import io.reisub.unethicalite.utils.tasks.Task;
import io.reisub.unethicalite.woodcutting.Config;
import io.reisub.unethicalite.woodcutting.Woodcutting;
import java.util.ArrayDeque;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.client.Static;

@Singleton
public class Chop extends Task {

  private final Woodcutting plugin;
  private final Config config;
  private ArrayDeque<WorldPoint> treePositions;
  @Getter
  @Setter
  private WorldPoint currentTreePosition;

  @Inject
  public Chop(Woodcutting plugin, Config config) {
    this.plugin = plugin;
    this.config = config;
    resetQueue();
  }

  @Override
  public String getStatus() {
    return "Chopping";
  }

  @Override
  public boolean validate() {
    final boolean isReady = currentTreePosition == null
        || TileObjects.getFirstAt(
        currentTreePosition.dx(config.location().getXoffset()).dy(config.location().getYoffset()),
        Predicates.ids(config.location().getTreeIds())) == null;

    return
        (!Inventory.isFull() || Static.getClient().getTickCount() - plugin.getLastBankTick() <= 2)
        && isReady;
  }

  @Override
  public int execute() {
    final TileObject tree = getTree();

    if (tree == null) {
      return 1;
    }

    currentTreePosition = tree.getWorldLocation();
    GameThread.invoke(() -> tree.interact("Chop down"));
    return 1;
  }

  private TileObject getTree() {
    if (treePositions == null || treePositions.isEmpty()) {
      // return the nearest tree
      return TileObjects.getNearest(Predicates.ids(config.location().getTreeIds()));
    } else {
      if (config.location().isOrdered()) {
        // return the first tree found according to the order of the tree positions
        while (!treePositions.isEmpty()) {
          WorldPoint point = treePositions.poll();

          final TileObject tree = TileObjects.getFirstAt(
              point,
              Predicates.ids(config.location().getTreeIds())
          );

          if (tree != null) {
            if (treePositions.isEmpty()) {
              resetQueue();
            }

            return tree;
          }
        }

        resetQueue();
      } else {
        // return the nearest tree with a position specified in tree positions
        return TileObjects.getNearest(
            o -> {
              if (!config.location().getTreeIds().contains(o.getId())) {
                return false;
              }

              for (WorldPoint point : treePositions) {
                if (o.getWorldLocation().equals(point)) {
                  return true;
                }
              }

              return false;
            });
      }
    }

    return null;
  }

  private void resetQueue() {
    treePositions = new ArrayDeque<>();
    treePositions.addAll(config.location().getTreePositions());
  }
}
