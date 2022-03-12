package io.reisub.unethicalite.shopper.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.items.Inventory;
import io.reisub.unethicalite.shopper.Shopper;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ItemID;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;

public class FillBag extends Task {
    @Inject
    private Shopper plugin;

    @Override
    public String getStatus() {
        return "Filling bag";
    }

    @Override
    public boolean validate() {
        return (Inventory.contains(ItemID.COAL_BAG_12019)
                && Inventory.contains(ItemID.COAL))
                && plugin.getCoalInBag() < 26;
    }

    @Override
    public void execute() {
        int coalCount = Inventory.getCount(ItemID.COAL);
        plugin.setCoalInBag(plugin.getCoalInBag() + coalCount);

        Inventory.getFirst(ItemID.COAL_BAG_12019).interact("Fill");
        Time.sleepTicksUntil(() -> Inventory.getCount(ItemID.COAL) < coalCount, 3);
    }

    @Subscribe
    private void onChatMessage(ChatMessage event) {
        if (event.getMessage().equals("The coal bag is now empty.")) {
            plugin.setCoalInBag(0);
        }
    }
}
