package io.reisub.unethicalite.herblore.tasks;

import dev.hoot.api.items.Inventory;
import dev.hoot.api.packets.ItemPackets;
import io.reisub.unethicalite.herblore.Herblore;
import io.reisub.unethicalite.herblore.HerbloreTask;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;

import javax.inject.Inject;

public class Clean extends Task {
    @Inject
    private Herblore plugin;

    @Override
    public String getStatus() {
        return "Cleaning herbs";
    }

    @Override
    public boolean validate() {
        HerbloreTask task = plugin.getConfig().task();

        return (task == HerbloreTask.CLEAN_HERBS || task == HerbloreTask.MAKE_UNFINISHED || task == HerbloreTask.MAKE_POTION || task == HerbloreTask.TAR_HERBS)
                && plugin.getCurrentActivity() == Activity.IDLE
                && Inventory.contains(plugin.getGrimyHerbIds());
    }

    @Override
    public void execute() {
        plugin.setActivity(Activity.CLEANING_HERBS);
        Inventory.getAll(plugin.getGrimyHerbIds()).forEach((i) -> i.interact("Clean"));
    }
}
