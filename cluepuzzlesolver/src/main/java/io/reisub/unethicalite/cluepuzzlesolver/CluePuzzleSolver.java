package io.reisub.unethicalite.cluepuzzlesolver;

import static net.runelite.client.plugins.puzzlesolver.solver.PuzzleSolver.BLANK_TILE_VALUE;
import static net.runelite.client.plugins.puzzlesolver.solver.PuzzleSolver.DIMENSION;

import io.reisub.unethicalite.utils.Utils;
import java.util.ArrayDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuAction;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.puzzlesolver.PuzzleSolverPlugin;
import net.runelite.client.plugins.puzzlesolver.solver.PuzzleSolver;
import net.runelite.client.plugins.puzzlesolver.solver.PuzzleState;
import net.runelite.client.plugins.puzzlesolver.solver.heuristics.ManhattanDistance;
import net.runelite.client.plugins.puzzlesolver.solver.pathfinding.IDAStar;
import net.unethicalite.api.widgets.Widgets;
import net.unethicalite.client.Static;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Clue Puzzle Solver",
    description = "Like one of those weird Rubik's cube enthusiasts",
    enabledByDefault = false
)
@PluginDependency(Utils.class)
@PluginDependency(PuzzleSolverPlugin.class)
@Slf4j
@Extension
public class CluePuzzleSolver extends Plugin {

  private ScheduledExecutorService executorService;
  private PuzzleSolver solver;
  private Future<?> solverFuture;
  private ArrayDeque<Integer> solution;

  @Override
  public void startUp() {
    executorService = Executors.newSingleThreadScheduledExecutor();
  }

  @Override
  public void shutDown() {
    executorService = null;
  }

  @Subscribe
  private void onGameTick(GameTick event) {
    final ItemContainer container = Static.getClient().getItemContainer(InventoryID.PUZZLE_BOX);

    final Widget puzzleBox = Widgets.get(WidgetInfo.PUZZLE_BOX);

    if (container == null || puzzleBox == null) {
      solver = null;
      solverFuture = null;
      solution = null;
      return;
    }

    if (solver == null) {
      solve(getItemIds(container));
    }

    if (solver != null) {
      if (solver.hasSolution() && solution == null) {
        solution = new ArrayDeque<>(solver.getStepCount() - 1);

        for (int i = 1; i < solver.getStepCount(); i++) {
          solution.add(solver.getStep(i).getEmptyPiece());
        }
      }

      if (solution != null && !solution.isEmpty()) {
        for (int i = 0; i < 8; i++) {
          final int index = solution.poll();
          final Widget piece = puzzleBox.getChild(index);

          if (piece != null) {
            piece.interact(
                1,
                MenuAction.CC_OP.getId(),
                piece.getIndex(),
                piece.getId()
            );
          }

          if (solution.isEmpty()) {
            break;
          }
        }
      }
    }
  }

  private void solve(int[] items) {
    if (solverFuture != null) {
      solverFuture.cancel(true);
    }

    PuzzleState puzzleState = new PuzzleState(items);

    solver = new PuzzleSolver(new IDAStar(new ManhattanDistance()), puzzleState);

    solverFuture = executorService.submit(solver);
  }

  private int[] getItemIds(ItemContainer container) {
    int[] itemIds = new int[DIMENSION * DIMENSION];

    Item[] items = container.getItems();

    for (int i = 0; i < items.length; i++) {
      itemIds[i] = items[i].getId();
    }

    // If blank is in the last position, items doesn't contain it, so let's add it manually
    if (itemIds.length > items.length) {
      itemIds[items.length] = BLANK_TILE_VALUE;
    }

    return convertToSolverFormat(itemIds);
  }

  private int[] convertToSolverFormat(int[] items) {
    int lowestId = Integer.MAX_VALUE;

    int[] convertedItems = new int[items.length];

    for (int id : items) {
      if (id == BLANK_TILE_VALUE) {
        continue;
      }

      if (lowestId > id) {
        lowestId = id;
      }
    }

    for (int i = 0; i < items.length; i++) {
      if (items[i] != BLANK_TILE_VALUE) {
        int value = items[i] - lowestId;

        convertedItems[i] = value;
      } else {
        convertedItems[i] = BLANK_TILE_VALUE;
      }
    }

    return convertedItems;
  }
}
