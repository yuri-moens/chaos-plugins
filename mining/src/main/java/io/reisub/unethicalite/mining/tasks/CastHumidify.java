package io.reisub.unethicalite.mining.tasks;

import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.magic.SpellBook;
import net.unethicalite.api.widgets.Widgets;

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
  public int execute() {
    Widget widget = Widgets.get(SpellBook.Lunar.HUMIDIFY.getWidget());
    widget.interact(1, MenuAction.CC_OP.getId(), -1, SpellBook.Lunar.HUMIDIFY.getWidget().getId());

    Time.sleepTicksUntil(() -> Inventory.contains(ItemID.WATERSKIN4), 5);
    return 1;
  }
}
