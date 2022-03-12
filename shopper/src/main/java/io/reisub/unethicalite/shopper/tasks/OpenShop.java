package io.reisub.unethicalite.shopper.tasks;

import dev.hoot.api.commons.Rand;
import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.NPCs;
import dev.hoot.api.game.GameThread;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.items.Shop;
import dev.hoot.api.movement.Movement;
import io.reisub.unethicalite.shopper.Config;
import io.reisub.unethicalite.shopper.Shopper;
import io.reisub.unethicalite.utils.api.CMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.NPC;

import javax.inject.Inject;

public class OpenShop extends Task {
    @Inject
    private Shopper plugin;

    @Inject
    private Config config;

    @Override
    public String getStatus() {
        return "Opening shop";
    }

    @Override
    public boolean validate() {
        return !Inventory.isFull()
                && !Shop.isOpen();
    }

    @Override
    public void execute() {
        if (!Movement.isRunEnabled() && Movement.getRunEnergy() >= Rand.nextInt(20, 40)) {
            Movement.toggleRun();
        }

        if (plugin.getNpcLocation() != null) {
            CMovement.walkTo(plugin.getNpcLocation(), 1);
        }

        NPC npc = NPCs.getNearest(config.npcName());
        if (npc == null) {
            return;
        }

        GameThread.invoke(() -> npc.interact("Trade"));
        Time.sleepTicksUntil(Shop::isOpen, 20);
    }
}
