package io.reisub.unethicalite.tempoross.tasks;

import dev.hoot.api.commons.Rand;
import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.NPCs;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.packets.MovementPackets;
import dev.hoot.api.packets.TileObjectPackets;
import io.reisub.unethicalite.tempoross.Tempoross;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;

import javax.inject.Inject;

public class Cook extends Task {
    @Inject
    private Tempoross plugin;

    @Override
    public String getStatus() {
        return "Cooking";
    }

    @Override
    public boolean validate() {
        if (!plugin.isInTemporossArea()) return false;

        if (plugin.getRawFish() == 0) return false;

        if (plugin.getCurrentActivity() == Activity.ATTACKING) return false;

        if (plugin.getPhase() == 1
                && plugin.getEnergy() < 100
                && plugin.getRawFish() + plugin.getCookedFish() >= 19
                && plugin.getCurrentActivity() == Activity.FISHING
                && (93 - plugin.getStormIntensity()) > plugin.getCookedFishRequired() - plugin.getCookedFish()
        ) return false;

        if (plugin.getPhase() == 1
                && plugin.getEnergy() < 100
                && plugin.getRawFish() + plugin.getCookedFish() >= 19
                && (plugin.getCurrentActivity() == Activity.FISHING || plugin.getCurrentActivity() == Activity.IDLE)
        ) return true;

        if (plugin.getPhase() == 2
                && plugin.getCookedFishRequired() > 0
                && plugin.getCookedFishRequired() != 19
                && plugin.getRawFish() + plugin.getCookedFish() >= plugin.getCookedFishRequired()
                && (plugin.getCurrentActivity() == Activity.FISHING || plugin.getCurrentActivity() == Activity.IDLE)
        ) return true;

        NPC doubleSpot = NPCs.getNearest((n) -> n.getId() == NpcID.FISHING_SPOT_10569 && plugin.getIslandArea().contains(n));

        if (doubleSpot != null
                && !Inventory.isFull()
                && !(plugin.getPhase() == 1 && plugin.getCookedFishRequired() == 19 && plugin.getRawFish() + plugin.getCookedFish() >= 19)
        ) return false;

        if (plugin.getCurrentActivity() == Activity.FISHING
                && Inventory.getCount(ItemID.RAW_HARPOONFISH) >= 9
                && doubleSpot == null
        ) return true;

        return plugin.getCurrentActivity() == Activity.IDLE;
    }

    @Override
    public void execute() {
        WorldPoint target = plugin.getDudiPos().dx(7).dy(16);

        if (Players.getLocal().getWorldLocation().getY() < target.getY() - 5) {
            MovementPackets.sendMovement(target.dx(Rand.nextInt(-2, 3)).dy(Rand.nextInt(-2, 3)));

            if (!Time.sleepUntil(() -> Players.getLocal().isMoving(), 1500)) {
                return;
            }

            Time.sleepUntil(() -> Players.getLocal().getWorldLocation().getY() >= target.getY() - Rand.nextInt(4, 6) || plugin.isWaveIncoming(), 10000);
        }

        NPC doubleSpot = NPCs.getNearest((n) -> n.getId() == NpcID.FISHING_SPOT_10569 && plugin.getIslandArea().contains(n));

        if (doubleSpot != null
                && !Inventory.isFull()
                && !(plugin.getPhase() == 1 && plugin.getCookedFishRequired() == 19 && plugin.getRawFish() + plugin.getCookedFish() >= 19)
        ) return;

        if (plugin.isWaveIncoming()) return;

        TileObject shrine = TileObjects.getNearest(ObjectID.SHRINE_41236);
        if (shrine == null) return;

        TileObjectPackets.tileObjectFirstOption(shrine, false);
        Time.sleepUntil(() -> plugin.getCurrentActivity() == Activity.COOKING || plugin.isWaveIncoming(), 10000);
    }
}
