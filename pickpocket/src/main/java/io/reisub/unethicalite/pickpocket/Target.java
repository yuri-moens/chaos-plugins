package io.reisub.unethicalite.pickpocket;

import com.google.common.collect.ImmutableSet;
import dev.hoot.api.entities.Players;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.NpcID;
import net.runelite.api.coords.WorldPoint;

import java.util.Set;

@AllArgsConstructor
@Getter
public enum Target {
    MASTER_FARMER(
            ImmutableSet.of(NpcID.MASTER_FARMER, NpcID.MASTER_FARMER_5731),
            ImmutableSet.of(new WorldPoint(3082, 3249, 0)),
            ImmutableSet.of(new WorldPoint(3092, 3245, 0))
    );

    private final Set<Integer> ids;
    private final Set<WorldPoint> pickpocketLocations;
    private final Set<WorldPoint> bankLocations;

    public WorldPoint getNearestPickpocketLocation() {
        WorldPoint nearest = null;

        for (WorldPoint location : pickpocketLocations) {
            if (nearest == null) {
                nearest = location;
                continue;
            }

            if (Players.getLocal().distanceTo(location) < Players.getLocal().distanceTo(nearest)) {
                nearest = location;
            }
        }

        return nearest;
    }

    public WorldPoint getNearestBankLocation() {
        WorldPoint nearest = null;

        for (WorldPoint location : bankLocations) {
            if (nearest == null) {
                nearest = location;
                continue;
            }

            if (Players.getLocal().distanceTo(location) < Players.getLocal().distanceTo(nearest)) {
                nearest = location;
            }
        }

        return nearest;
    }
}
