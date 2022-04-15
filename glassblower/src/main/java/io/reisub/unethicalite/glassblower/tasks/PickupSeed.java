package io.reisub.unethicalite.glassblower.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileItems;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.glassblower.Glassblower;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ItemID;

import javax.inject.Inject;

public class PickupSeed extends Task {
    @Inject
    private Glassblower plugin;

    @Override
    public String getStatus() {
        return "Picking up seed";
    }

    @Override
    public boolean validate() {
        return Players.getLocal().getWorldLocation().getRegionID() == Glassblower.FOSSIL_ISLAND_UNDERWATER_REGION
                && TileItems.getNearest(ItemID.SEAWEED_SPORE) != null;
    }

    @Override
    public void execute() {
        plugin.setActivity(Activity.IDLE);

        int quantity = Inventory.getCount(true, ItemID.SEAWEED_SPORE);

        TileItems.getNearest(ItemID.SEAWEED_SPORE).interact("Take");

        Time.sleepTicksUntil(
                () -> Inventory.getCount(true, ItemID.SEAWEED_SPORE) > quantity
                        || Players.getLocal().getWorldLocation().getRegionID() == Glassblower.FOSSIL_ISLAND_SMALL_ISLAND_REGION
                , 30);
    }
}
