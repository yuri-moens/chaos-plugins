package io.reisub.unethicalite.herblore.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.widgets.Production;
import io.reisub.unethicalite.herblore.Herblore;
import io.reisub.unethicalite.herblore.HerbloreTask;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.ItemID;

import javax.inject.Inject;
import java.util.List;

public class MakeUnfinished extends Task {
    @Inject
    private Herblore plugin;

    @Override
    public String getStatus() {
        return "Making unfinished potions";
    }

    @Override
    public boolean validate() {
        HerbloreTask task = plugin.getConfig().task();

        return (task == HerbloreTask.MAKE_UNFINISHED || task == HerbloreTask.MAKE_POTION)
                && plugin.getCurrentActivity() == Activity.IDLE
                && Inventory.contains(plugin.getCleanHerbIds())
                && Inventory.contains(ItemID.VIAL_OF_WATER, ItemID.VIAL_OF_BLOOD, ItemID.COCONUT_MILK);
    }

    @Override
    public void execute() {
        plugin.setActivity(Activity.CREATING_UNFINISHED_POTIONS);

        List<Item> herbs = Inventory.getAll(plugin.getCleanHerbIds());
        List<Item> bases = Inventory.getAll(ItemID.VIAL_OF_WATER, ItemID.VIAL_OF_BLOOD, ItemID.COCONUT_MILK);

        if (herbs.size() == 0 || bases.size() == 0) return;

        int quantity = Math.min(herbs.size(), bases.size());

        herbs.get(0).useOn(bases.get(0));
        Time.sleepTicksUntil(Production::isOpen, 5);

        Production.chooseOption(1);
    }
}
