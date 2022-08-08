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

    if (config.location().getTreePositions() != null) {
      resetQueue();
    }
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
          o -> config.location().getTreeIds().contains(o.getId())
            && o.hasAction("Chop down")) == null;

    return
        (!Inventory.isFull() || Static.getClient().getTickCount() - plugin.getLastBankTick() <= 2)
            && isReady;
  }

  @Override
  public void execute() {
    final TileObject tree = getTree();

    if (tree == null) {
      return;
    }

    currentTreePosition = tree.getWorldLocation();
    GameThread.invoke(() -> tree.interact("Chop down"));
  }

  private TileObject getTree() {
    if (treePositions == null || treePositions.isEmpty()) {
      // return the nearest tree
      return TileObjects.getNearest(o -> config.location().getTreeIds().contains(o.getId())
          && o.hasAction("Chop down"));
    } else {
      if (config.location().isOrdered()) {
        // return the first tree found according to the order of the tree positions
        while (!treePositions.isEmpty()) {
          WorldPoint point = treePositions.poll();

          final TileObject tree = TileObjects.getFirstAt(
              point,
              o -> config.location().getTreeIds().contains(o.getId())
                  && o.hasAction("Chop down")
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
              if (!config.location().getTreeIds().contains(o.getId())
                  || !o.hasAction("Chop down")) {
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
