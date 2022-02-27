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
import net.runelite.api.ItemID;

import javax.inject.Inject;
import java.util.List;

public class TarHerbs extends Task {
    @Inject
    private Herblore plugin;

    @Override
    public String getStatus() {
        return "Tarring herbs";
    }

    @Override
    public boolean validate() {
        return plugin.getConfig().task() == HerbloreTask.TAR_HERBS
                && plugin.getCurrentActivity() == Activity.IDLE
                && Inventory.contains(ItemID.PESTLE_AND_MORTAR)
                && Inventory.getCount(ItemID.SWAMP_TAR) >= 15
                && Inventory.contains(plugin.getCleanTarHerbIds());
    }

    @Override
    public void execute() {
        plugin.setActivity(Activity.TARRING_HERBS);

        List<Item> herbs = Inventory.getAll(plugin.getCleanTarHerbIds());
        Item swampTar = Inventory.getFirst(ItemID.SWAMP_TAR);

        int quantity = Math.min(herbs.size(), swampTar.getQuantity() / 15);

        ItemPackets.useItemOnItem(swampTar, herbs.get(0));
        Time.sleepTicksUntil(Production::isOpen, 5);

        WidgetPackets.queueResumePauseWidgetPacket(Constants.MAKE_FIRST_ITEM_WIDGET_ID, quantity);
    }
}