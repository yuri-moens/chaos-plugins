package io.reisub.unethicalite.smithing.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.items.Bank;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.packets.TileObjectPackets;
import dev.hoot.api.packets.WidgetPackets;
import dev.hoot.api.widgets.Widgets;
import io.reisub.unethicalite.smithing.Config;
import io.reisub.unethicalite.smithing.Smithing;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.AllArgsConstructor;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

@AllArgsConstructor
public class Smith extends Task {
    private final Smithing plugin;
    private final Config config;

    @Override
    public String getStatus() {
        return "Smithing";
    }

    @Override
    public boolean validate() {
        return plugin.getCurrentActivity() == Activity.IDLE
                && Inventory.getCount(config.metal().getBarId()) >= config.product().getRequiredBars();
    }

    @Override
    public void execute() {
        TileObject anvil = TileObjects.getNearest((o) -> Constants.ANVIL_IDS.contains(o.getId()));
        if (anvil == null) {
            return;
        }

        TileObjectPackets.tileObjectFirstOption(anvil, false);
        Time.sleepTicksUntil(() -> Widgets.isVisible(Widgets.get(WidgetInfo.SMITHING_INVENTORY_ITEMS_CONTAINER)), 15);

        Widget productWidget = Widgets.get(312, config.product().getInterfaceId());
        if (productWidget == null) {
            return;
        }

        WidgetPackets.widgetFirstOption(productWidget);
        Time.sleepTicksUntil(() -> plugin.getCurrentActivity() == Activity.SMITHING, 10);
    }
}
