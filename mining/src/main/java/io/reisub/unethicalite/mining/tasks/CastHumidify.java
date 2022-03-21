package io.reisub.unethicalite.mining.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.magic.Lunar;
import dev.hoot.api.widgets.Widgets;
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
                && !Inventory.contains(ItemID.WATERSKIN1, ItemID.WATERSKIN2, ItemID.WATERSKIN3, ItemID.WATERSKIN4);
    }

    @Override
    public void execute() {
        Widget widget = Widgets.get(Lunar.HUMIDIFY.getWidget());
        widget.interact(1, MenuAction.CC_OP.getId(), -1, Lunar.HUMIDIFY.getWidget().getId());

        Time.sleepTicks(3);
    }
}
