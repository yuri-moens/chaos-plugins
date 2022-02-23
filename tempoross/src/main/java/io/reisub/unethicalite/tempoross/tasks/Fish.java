package io.reisub.unethicalite.tempoross.tasks;

import dev.hoot.api.commons.Rand;
import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.NPCs;
import dev.hoot.api.entities.Players;
import dev.hoot.api.game.Combat;
import dev.hoot.api.items.Equipment;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.movement.Movement;
import io.reisub.unethicalite.tempoross.Tempoross;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.coords.WorldPoint;

import javax.inject.Inject;

public class Fish extends Task {
    @Inject
    private Tempoross plugin;

    private NPC spot;

    @Override
    public String getStatus() {
        return "Fishing";
    }

    @Override
    public boolean validate() {
        if (!plugin.isInTemporossArea() || Inventory.isFull()) return false;

        if (plugin.getPhase() >= 4) return false;

        spot = NPCs.getNearest(NpcID.FISHING_SPOT_10569);

        if (plugin.getCurrentActivity() == Activity.FISHING) {
            if (spot != null && Players.getLocal().getInteracting() != spot) {
                return true;
            }
        }

        if (plugin.getCurrentActivity() == Activity.COOKING && spot != null) {
            if (plugin.getPhase() == 1
                    && plugin.getEnergy() < 100
                    && plugin.getRawFish() + plugin.getCookedFish() >= 19) {
                return false;
            }

            if (plugin.getPhase() == 2
                    && plugin.getCookedFishRequired() > 0
                    && plugin.getCookedFishRequired() != 19
                    && plugin.getRawFish() + plugin.getCookedFish() >= plugin.getCookedFishRequired()) {
                return false;
            }

            return true;
        }

        return plugin.getCurrentActivity() == Activity.IDLE;
    }

    @Override
    public void execute() {
        WorldPoint target = plugin.getDudiPos().dx(7).dy(16);

        if (Players.getLocal().getWorldLocation().getY() < target.getY() - 5) {
            if (plugin.getPhase() == 1 && plugin.getEnergy() < 100) {
                Time.sleep(400, 600);
            }

            Movement.walk(target.dx(Rand.nextInt(-2, 3)).dy(Rand.nextInt(-2, 3)));

            if (!Time.sleepUntil(() -> Players.getLocal().isMoving(), 1500)) {
                return;
            }

            Time.sleepUntil(() -> Players.getLocal().getWorldLocation().getY() >= target.getY() - Rand.nextInt(4, 6), 15000);
        }

        if (plugin.getPhase() >= 2) {
            Inventory.getAll(ItemID.BUCKET, ItemID.BUCKET_OF_WATER).forEach((i) -> i.interact("Drop"));
        }

        if (Combat.getSpecEnergy() == 100
                && Equipment.contains(ItemID.DRAGON_HARPOON, ItemID.DRAGON_HARPOON_OR, ItemID.INFERNAL_HARPOON, ItemID.INFERNAL_HARPOON_OR, ItemID.CRYSTAL_HARPOON)) {
            Combat.toggleSpec();
        }

        if (spot == null) {
            spot = NPCs.getNearest(NpcID.FISHING_SPOT_10565);
        }

        if (spot == null) return;

        spot.interact(0);
        Time.sleepUntil(() -> plugin.getCurrentActivity() == Activity.FISHING, 3000);
    }
}
