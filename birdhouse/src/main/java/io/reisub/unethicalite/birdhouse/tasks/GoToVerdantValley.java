package io.reisub.unethicalite.birdhouse.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.widgets.Widgets;
import io.reisub.unethicalite.birdhouse.BirdHouse;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.MenuAction;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

public class GoToVerdantValley extends Task {
  @Override
  public String getStatus() {
    return "Going to Verdant Valley";
  }

  @Override
  public boolean validate() {
    return BirdHouse.HILL_HOUSE.contains(Players.getLocal())
        && Inventory.getCount((i) -> Constants.LOG_IDS.contains(i.getId())) == 4;
  }

  @Override
  public void execute() {
    TileObject tree = TileObjects.getNearest(i -> Constants.MAGIC_MUSHTREE_IDS.contains(i.getId()));
    if (tree == null) {
      return;
    }

    GameThread.invoke(() -> tree.interact(0));
    Time.sleepTicksUntil(
        () -> Widgets.isVisible(Widgets.get(WidgetInfo.FOSSIL_MUSHROOM_TELEPORT)), 15);

    Widget mushroomValleyWidget = Widgets.get(WidgetInfo.FOSSIL_MUSHROOM_VALLEY);
    if (!Widgets.isVisible(mushroomValleyWidget)) {
      return;
    }

    mushroomValleyWidget.interact(
        0,
        MenuAction.WIDGET_TYPE_6.getId(),
        mushroomValleyWidget.getIndex(),
        mushroomValleyWidget.getId());

    Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().getRegionID() == 14906, 5);
    Time.sleepTicks(2);
  }
}
