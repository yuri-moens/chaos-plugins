package io.reisub.unethicalite.tempoross.tasks;

import dev.hoot.api.commons.Rand;
import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.NPCs;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.game.Combat;
import dev.hoot.api.items.Equipment;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.packets.MovementPackets;
import dev.hoot.api.widgets.Widgets;
import io.reisub.unethicalite.tempoross.Tempoross;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.*;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

        spot = getNearestSafeSpot(NpcID.FISHING_SPOT_10569);

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

            MovementPackets.sendMovement(target.dx(Rand.nextInt(-2, 3)).dy(Rand.nextInt(-2, 3)));

            if (!Time.sleepUntil(() -> Players.getLocal().isMoving(), 1500)) {
                return;
            }

            Time.sleepUntil(() -> Players.getLocal().getWorldLocation().getY() >= target.getY() - Rand.nextInt(4, 6) || plugin.isWaveIncoming(), 15000);
        }

        if (plugin.getPhase() >= 2) {
            Inventory.getAll(ItemID.BUCKET, ItemID.BUCKET_OF_WATER).forEach((i) -> i.interact("Drop"));
        }

        if (Combat.getSpecEnergy() == 100
                && Equipment.contains(ItemID.DRAGON_HARPOON, ItemID.DRAGON_HARPOON_OR, ItemID.INFERNAL_HARPOON, ItemID.INFERNAL_HARPOON_OR, ItemID.CRYSTAL_HARPOON)) {
            Widget spec = Widgets.get(160, 31);
            if (spec != null) {
                spec.interact(0);
            }
        }

        if (spot == null) {
            spot = getNearestSafeSpot(NpcID.FISHING_SPOT_10565);
        }

        if (spot == null) {
            return;
        }

        spot.interact(0);
        Time.sleepTick();
        Time.sleepUntil(() -> plugin.getCurrentActivity() == Activity.FISHING, 3000);
    }

    private NPC getNearestSafeSpot(int id) {
        List<TileObject> fires = TileObjects.getAll((o) -> (o.getId() == NullObjectID.NULL_41006 || o.getId() == 37582)
                && (plugin.getIslandArea().contains(o) || plugin.getBoatArea().contains(o)));

        if (fires.size() == 0) {
            return NPCs.getNearest(id);
        } else {
            List<WorldArea> unsafeAreas = new ArrayList<>();
            for (TileObject fire : fires) {
                unsafeAreas.add(new WorldArea(fire.getWorldLocation(), 2, 2));
            }

            List<NPC> spots = NPCs.getAll((n) -> n.getId() == id && plugin.getIslandArea().contains(n));
            spots.sort(Comparator.comparingInt(o -> o.distanceTo(Players.getLocal())));

            for (NPC spot : spots) {
                if (isSpotSafe(spot, unsafeAreas)) {
                    return spot;
                }
            }
        }

        return null;
    }

    private boolean isSpotSafe(NPC spot, List<WorldArea> unsafeAreas) {
        WorldPoint p = spot.getWorldLocation();

        for (WorldArea unsafeArea : unsafeAreas) {
            if (
                    unsafeArea.contains(p.dx(1))
                    || unsafeArea.contains(p.dx(-1))
                    || unsafeArea.contains(p.dy(1))
                    || unsafeArea.contains(p.dy(-1))
            ) return false;
        }

        return true;
    }
}
