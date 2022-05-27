package io.reisub.unethicalite.barrows.tasks;

import com.google.common.collect.ImmutableMap;
import io.reisub.unethicalite.barrows.Room;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.Map;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.widgets.Widgets;

public class SolvePuzzle extends Task {
  private static final Map<WidgetInfo, WidgetInfo> POSSIBLE_SOLUTIONS =
      ImmutableMap.of(
          WidgetInfo.BARROWS_PUZZLE_ANSWER1, WidgetInfo.BARROWS_PUZZLE_ANSWER1_CONTAINER,
          WidgetInfo.BARROWS_PUZZLE_ANSWER2, WidgetInfo.BARROWS_PUZZLE_ANSWER2_CONTAINER,
          WidgetInfo.BARROWS_PUZZLE_ANSWER3, WidgetInfo.BARROWS_PUZZLE_ANSWER3_CONTAINER);

  private Widget puzzleAnswer = null;

  @Override
  public String getStatus() {
    return "Solving puzzle";
  }

  @Override
  public boolean validate() {
    return puzzleAnswer != null;
  }

  @Override
  public void execute() {
    GameThread.invoke(() -> puzzleAnswer.interact("Select"));
    Time.sleepTick();

    GameThread.invoke(() -> TileObjects.getNearest(Room.DOOR_PREDICATE).interact(0));
    Time.sleepTicksUntil(Room::isInCorridor, 5);

    puzzleAnswer = null;
  }

  @Subscribe
  private void onWidgetLoaded(WidgetLoaded event) {
    if (event.getGroupId() == WidgetID.BARROWS_PUZZLE_GROUP_ID) {
      int answer = Widgets.get(WidgetInfo.BARROWS_FIRST_PUZZLE).getModelId() - 3;

      for (Map.Entry<WidgetInfo, WidgetInfo> entry : POSSIBLE_SOLUTIONS.entrySet()) {
        Widget widgetToCheck = Widgets.get(entry.getKey());

        if (widgetToCheck != null && widgetToCheck.getModelId() == answer) {
          puzzleAnswer = Widgets.get(entry.getValue());
          break;
        }
      }
    }
  }
}
