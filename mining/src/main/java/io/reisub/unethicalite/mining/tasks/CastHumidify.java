package io.reisub.unethicalite.mining.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.magic.SpellBook;
import dev.unethicalite.api.widgets.Widgets;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.widgets.Widget;

public class CastHumidify extends Task {
  @Override
  public String getStatus() {
    return "Casting Humidify";
  }

  @Override
  public boolean validate() {
    return Inventory.contains(ItemID.WATERSKIN0)
        && !Inventory.contains(
            ItemID.WATERSKIN1, ItemID.WATERSKIN2, ItemID.WATERSKIN3, ItemID.WATERSKIN4)
        && SpellBook.Lunar.HUMIDIFY.canCast();
  }

  @Override
  public void execute() {
    Widget widget = Widgets.get(SpellBook.Lunar.HUMIDIFY.getWidget());
    widget.interact(1, MenuAction.CC_OP.getId(), -1, SpellBook.Lunar.HUMIDIFY.getWidget().getId());

    Time.sleepTicksUntil(() -> Inventory.contains(ItemID.WATERSKIN4), 5);
  }
}
