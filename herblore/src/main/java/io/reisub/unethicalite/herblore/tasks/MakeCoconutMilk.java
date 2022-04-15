package io.reisub.unethicalite.herblore.tasks;

import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.herblore.Herblore;
import io.reisub.unethicalite.herblore.HerbloreTask;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.ItemID;

import javax.inject.Inject;
import java.util.List;

public class MakeCoconutMilk extends Task {
    @Inject
    private Herblore plugin;

    @Override
    public String getStatus() {
        return "Making coconut milk";
    }

    @Override
    public boolean validate() {
        return plugin.getConfig().task() == HerbloreTask.MAKE_COCONUT_MILK
                && plugin.getCurrentActivity() == Activity.IDLE
                && Inventory.contains(ItemID.HAMMER, ItemID.IMCANDO_HAMMER)
                && Inventory.contains(ItemID.COCONUT)
                && Inventory.contains(ItemID.VIAL);
    }

    @Override
    public void execute() {
        plugin.setActivity(Activity.MAKING_COCONUT_MILK);

        List<Item> coconuts = Inventory.getAll(ItemID.COCONUT);
        Item hammer = Inventory.getFirst(ItemID.HAMMER, ItemID.IMCANDO_HAMMER);

        coconuts.forEach(hammer::useOn);
    }
}
