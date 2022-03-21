package io.reisub.unethicalite.superglassmake.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileItems;
import dev.hoot.api.items.Inventory;
import dev.hoot.bot.managers.Static;
import io.reisub.unethicalite.superglassmake.Config;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ItemID;
import net.runelite.api.TileItem;

import javax.inject.Inject;

public class PickupGlass extends Task {
    @Inject
    private Config config;

    private int last;

    @Override
    public String getStatus() {
        return "Picking up glass";
    }

    @Override
    public boolean validate() {
        return config.pickupGlass()
                && !Inventory.isFull()
                && last + 3 < Static.getClient().getTickCount()
                && !TileItems.getAt(Players.getLocal().getWorldLocation(), ItemID.MOLTEN_GLASS).isEmpty();
    }

    @Override
    public void execute() {
        TileItems.getAt(Players.getLocal().getWorldLocation(), ItemID.MOLTEN_GLASS).forEach((i) -> {
            i.interact(0);
            Time.sleepTick();
        });

        last = Static.getClient().getTickCount();
    }
}
