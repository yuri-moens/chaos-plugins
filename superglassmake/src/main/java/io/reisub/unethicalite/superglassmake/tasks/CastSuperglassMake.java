package io.reisub.unethicalite.superglassmake.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileItems;
import dev.hoot.api.items.Bank;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.magic.Lunar;
import dev.hoot.api.magic.Magic;
import io.reisub.unethicalite.superglassmake.Config;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ItemID;
import net.runelite.api.TileItem;

import javax.inject.Inject;
import java.util.List;

public class CastSuperglassMake extends Task {
    @Inject
    private Config config;

    @Override
    public String getStatus() {
        return "Casting Superglass Make";
    }

    @Override
    public boolean validate() {
        return Inventory.contains(ItemID.BUCKET_OF_SAND)
                && Inventory.contains(ItemID.SODA_ASH, ItemID.GIANT_SEAWEED)
                && !Bank.isOpen();
    }

    @Override
    public void execute() {
        Magic.cast(Lunar.SUPERGLASS_MAKE);
        Time.sleepTicksUntil((() -> Inventory.contains(ItemID.MOLTEN_GLASS)), 5);
        Time.sleepTicks(3);

        if (config.pickupGlass()) {
            List<TileItem> glass = TileItems.getAt(Players.getLocal().getWorldLocation(), ItemID.MOLTEN_GLASS);
            for (TileItem tileItem : glass.subList(0, Math.min(glass.size(), Inventory.getFreeSlots()))) {
                tileItem.interact("Take");
                Time.sleepTick();
            }
        }
    }
}
