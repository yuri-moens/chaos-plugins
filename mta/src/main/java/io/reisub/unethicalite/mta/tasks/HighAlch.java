package io.reisub.unethicalite.mta.tasks;

import dev.hoot.api.entities.Players;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.magic.Magic;
import dev.hoot.api.magic.Regular;
import dev.hoot.api.widgets.Widgets;
import dev.hoot.bot.managers.Static;
import io.reisub.unethicalite.mta.Mta;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.plugins.mta.alchemy.AlchemyItem;

public class HighAlch extends Task {
    private int last;
    private AlchemyItem best = null;

    @Override
    public String getStatus() {
        return "Casting high alchemy";
    }

    @Override
    public boolean validate() {
        best = getBest();

        return Static.getClient().getTickCount() > last + 3
                && Utils.isInRegion(Mta.MTA_REGION)
                && Players.getLocal().getWorldLocation().getPlane() == 2
                && best != null
                && (!Inventory.contains("Coins") || Inventory.getCount(true, "Coins") < 10000)
                && Inventory.contains(best.getId());
    }

    @Override
    public void execute() {
        Item item = Inventory.getFirst(best.getId());
        if (item == null) {
            return;
        }

        Magic.cast(Regular.HIGH_LEVEL_ALCHEMY, item);

        last = Static.getClient().getTickCount();
    }

    private AlchemyItem getBest() {
        for (int i = 0; i < 5; i++) { // 12
            Widget textWidget = Widgets.get(WidgetID.MTA_ALCHEMY_GROUP_ID, 7 + i);
            if (textWidget == null) {
                return null;
            }

            String item = textWidget.getText();
            Widget pointsWidget = Widgets.get(WidgetID.MTA_ALCHEMY_GROUP_ID, 12 + i);
            int points = Integer.parseInt(pointsWidget.getText());

            if (points == 30) {
                return AlchemyItem.find(item);
            }
        }

        return null;
    }
}
