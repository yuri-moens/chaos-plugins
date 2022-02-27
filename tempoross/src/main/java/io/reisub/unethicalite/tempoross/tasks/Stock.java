package io.reisub.unethicalite.tempoross.tasks;

import dev.hoot.api.commons.Rand;
import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.NPCs;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.packets.MovementPackets;
import dev.hoot.api.packets.NPCPackets;
import io.reisub.unethicalite.tempoross.Tempoross;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.NullObjectID;

import javax.inject.Inject;

public class Stock extends Task {
    @Inject
    private Tempoross plugin;

    @Override
    public String getStatus() {
        return "Stocking cannon";
    }

    @Override
    public boolean validate() {
        if (!plugin.isInTemporossArea()) return false;

        if (plugin.getCurrentActivity() == Activity.REPAIRING || plugin.getCurrentActivity() == Activity.TETHERING_MAST) return false;

        if (TileObjects.getNearest((o) -> o.getId() == NullObjectID.NULL_41006 && (plugin.getIslandArea().contains(o) || plugin.getBoatArea().contains(o))) != null) {
            return false;
        }

        // stock cannon for the first time
        // bring energy to 4%
        if (plugin.getPhase() == 1
                && plugin.getCookedFish() >= plugin.getCookedFishRequired()
                && plugin.getEnergy() == 100
                && plugin.getCurrentActivity() != Activity.STOCKING_CANNON
        ) return true;

        // stock cannon for the second time
        // trigger first phase change
        // bring energy to 4% or as close as possible
        if (plugin.getPhase() == 1
                && plugin.getEnergy() < 100
                && (plugin.getStormIntensity() >= 94 || plugin.getCookedFish() >= plugin.getCookedFishRequired())
                && plugin.getCurrentActivity() != Activity.STOCKING_CANNON
        )
            return true;

        // stock cannon at phase 2
        // bring energy to 4%
        if (plugin.getPhase() == 2
                && plugin.getEnergy() != 4
                && plugin.getCookedFishRequired() > 0
                && plugin.getCookedFishRequired() != 19
                && plugin.getCookedFish() >= plugin.getCookedFishRequired()
                && plugin.getCurrentActivity() != Activity.STOCKING_CANNON
        ) return true;

        // stock cannon at phase 2
        // trigger second phase change
        // bring energy to 4%
        if (plugin.getPhase() == 2
                && plugin.getCookedFish() >= plugin.getCookedFishRequired()
                && plugin.getCurrentActivity() != Activity.STOCKING_CANNON
        ) return true;

        // stock cannon at phase 3
        // trigger third phase change and potentially fourth phase change
        if (plugin.getPhase() >= 3
                && plugin.getCookedFish() > 0
                && plugin.getRawFish() == 0
                && Inventory.isFull()
                && plugin.getCurrentActivity() != Activity.STOCKING_CANNON
        ) return true;

        NPC northCrate = NPCs.getNearest(NpcID.AMMUNITION_CRATE);
        NPC southCrate = NPCs.getNearest(NpcID.AMMUNITION_CRATE_10577);

        if (northCrate != null && southCrate != null) {
            // swap ammunition box at phase 3
            if (plugin.getPhase() >= 4
                    && plugin.getCurrentActivity() == Activity.STOCKING_CANNON
                    && plugin.getCookedFish() > 0 && plugin.getCookedFish() < 15
                    && Players.getLocal().distanceTo(northCrate) < Players.getLocal().distanceTo(southCrate)
            ) return true;
        }

        return false;
    }

    @Override
    public void execute() {
        if (Players.getLocal().distanceTo(plugin.getDudiPos()) > 8) {
            MovementPackets.sendMovement(plugin.getDudiPos().dx(Rand.nextInt(-2, 3)).dy(Rand.nextInt(-2, 3)));

            Time.sleepUntil(() -> Players.getLocal().distanceTo(plugin.getDudiPos()) <= 8, 15000);
        }

        NPC fire = NPCs.getNearest((n) -> n.getId() == NpcID.FIRE_8643
                && (plugin.getIslandArea().contains(n) || plugin.getBoatArea().contains(n)));
        if (fire != null) return;

        NPC crate;

        if (plugin.getPhase() >= 4 && plugin.getCurrentActivity() == Activity.STOCKING_CANNON) {
            plugin.setActivity(Activity.IDLE);
            crate = NPCs.getNearest(NpcID.AMMUNITION_CRATE_10577);
        } else {
            if (NPCs.getNearest(NpcID.CANNONEER).getAnimation() == 7211) {
                crate = NPCs.getNearest(NpcID.AMMUNITION_CRATE_10577);
            } else {
                crate = NPCs.getNearest(NpcID.AMMUNITION_CRATE);
            }
        }

        if (crate == null) return;

        NPCPackets.npcFirstOption(crate, false);

        Time.sleepTicksUntil(() -> plugin.getCurrentActivity() == Activity.STOCKING_CANNON, 3);
    }
}
