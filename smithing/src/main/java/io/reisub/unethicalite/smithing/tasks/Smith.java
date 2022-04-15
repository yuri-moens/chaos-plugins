package io.reisub.unethicalite.smithing.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.widgets.Widgets;
import io.reisub.unethicalite.smithing.Config;
import io.reisub.unethicalite.smithing.Smithing;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.AllArgsConstructor;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

@AllArgsConstructor
public class Smith extends Task {
    private final Smithing plugin;
    private final Config config;

    private final static int PRIFDDINAS_REGION = 13150;
    private final static WorldPoint PRIFDDINAS_ANVIL_LOCATION = new WorldPoint(3287, 6055, 0);

    @Override
    public String getStatus() {
        return "Smithing";
    }

    @Override
    public boolean validate() {
        return plugin.getCurrentActivity() == Activity.IDLE
                && (Inventory.getCount(config.metal().getBarId()) >= config.product().getRequiredBars() || plugin.getPreviousActivity() == Activity.BANKING);
    }

    @Override
    public void execute() {
        TileObject anvil;
        if (Players.getLocal().getWorldLocation().getRegionID() == PRIFDDINAS_REGION) {
            anvil = TileObjects.getFirstAt(PRIFDDINAS_ANVIL_LOCATION, (o) -> Constants.ANVIL_IDS.contains(o.getId()));
        } else {
            anvil = TileObjects.getNearest((o) -> Constants.ANVIL_IDS.contains(o.getId()));
        }

        if (anvil == null) {
            return;
        }

        anvil.interact(0);
        Time.sleepTicksUntil(() -> Widgets.isVisible(Widgets.get(WidgetInfo.SMITHING_INVENTORY_ITEMS_CONTAINER)), 15);

        Widget productWidget = Widgets.get(312, config.product().getInterfaceId());
        if (productWidget == null) {
            return;
        }

        productWidget.interact(0);
        Time.sleepTicksUntil(() -> plugin.getCurrentActivity() == Activity.SMITHING, 10);
    }
}
