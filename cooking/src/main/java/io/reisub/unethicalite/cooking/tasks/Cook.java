package io.reisub.unethicalite.cooking.tasks;

import io.reisub.unethicalite.cooking.Config;
import io.reisub.unethicalite.cooking.Cooking;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Item;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.Game;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Production;

@RequiredArgsConstructor
public class Cook extends Task {
  private final Cooking plugin;
  private final Config config;

  private int last;

  @Override
  public String getStatus() {
    return "Cooking";
  }

  @Override
  public boolean validate() {
    int count = Inventory.getCount(config.foodId());

    return !config.sonicMode()
        && (plugin.getCurrentActivity() == Activity.IDLE || count == 1)
        && (count > 0 || plugin.getLastBank() + 1 >= Game.getClient().getTickCount())
        && Game.getClient().getTickCount() >= last + 3;
  }

  @Override
  public void execute() {
    TileObject oven = TileObjects.getNearest(ObjectID.CLAY_OVEN_21302, ObjectID.RANGE_31631);
    TileObject fire = TileObjects.getNearest("Fire");
    if (oven == null && fire == null) {
      return;
    }

    int count = Inventory.getCount(config.foodId());

    if (oven != null) {
      oven.interact(0);
    } else {
      Item rawFood = Inventory.getFirst(config.foodId());
      if (rawFood == null) {
        return;
      }

      rawFood.useOn(fire);
    }

    if (count > 1 || (count == 0 && plugin.getLastBank() + 1 >= Game.getClient().getTickCount())) {
      Time.sleepTicksUntil(Production::isOpen, 20);
    }

    if (Production.isOpen()) {
      Production.chooseOption(1);

      Time.sleepTicksUntil(() -> plugin.getCurrentActivity() == Activity.COOKING, 5);
    }

    last = Game.getClient().getTickCount();
  }
}
