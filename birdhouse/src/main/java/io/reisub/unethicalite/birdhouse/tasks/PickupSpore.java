package io.reisub.unethicalite.birdhouse.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileItems;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.birdhouse.BirdHouse;
import io.reisub.unethicalite.birdhouse.Config;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ItemID;
import net.runelite.api.TileItem;

@RequiredArgsConstructor
public class PickupSpore extends Task {
    private final BirdHouse plugin;
    private final Config config;

    private TileItem spore;

    @Override
    public String getStatus() {
        return "Picking up seaweed spore";
    }

    @Override
    public boolean validate() {
        return config.pickupSpores()
                && plugin.isUnderwater()
                && (spore = TileItems.getNearest(ItemID.SEAWEED_SPORE)) != null;
    }

    @Override
    public void execute() {
        int quantity = Inventory.getCount(true, ItemID.SEAWEED_SPORE);

        do {
            GameThread.invoke(() -> spore.interact("Take"));
            Time.sleep(250, 400);
        } while (!Players.getLocal().isMoving());

        Time.sleepTicksUntil(() -> Inventory.getCount(true, ItemID.SEAWEED_SPORE) > quantity, 30);
    }
}
