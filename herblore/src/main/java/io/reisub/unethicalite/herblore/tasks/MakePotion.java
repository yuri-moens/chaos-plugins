package io.reisub.unethicalite.herblore.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.packets.ItemPackets;
import dev.hoot.api.packets.WidgetPackets;
import dev.hoot.api.widgets.Production;
import io.reisub.unethicalite.herblore.Herblore;
import io.reisub.unethicalite.herblore.HerbloreTask;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;

import javax.inject.Inject;
import java.util.List;

public class MakePotion extends Task {
    @Inject
    private Herblore plugin;

    @Override
    public String getStatus() {
        return "Making potions";
    }

    @Override
    public boolean validate() {
        return plugin.getConfig().task() == HerbloreTask.MAKE_POTION
                && plugin.getCurrentActivity() == Activity.IDLE
                && Inventory.contains(plugin.getSecondaryIds())
                && Inventory.contains(plugin.getBaseIds());
    }

    @Override
    public void execute() {
        plugin.setActivity(Activity.CREATING_POTIONS);

        List<Item> secondaries = Inventory.getAll(plugin.getSecondaryIds());
        List<Item> bases = Inventory.getAll(plugin.getBaseIds());

        if (secondaries.size() == 0 || bases.size() == 0) return;

        int quantity = Math.min(secondaries.size(), bases.size());

        ItemPackets.useItemOnItem(secondaries.get(0), bases.get(0));
        Time.sleepTicksUntil(Production::isOpen, 5);

        WidgetPackets.queueResumePauseWidgetPacket(Constants.MAKE_FIRST_ITEM_WIDGET_ID, quantity);
    }
}
