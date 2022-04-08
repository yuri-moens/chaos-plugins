package io.reisub.unethicalite.pickpocket;

import com.google.common.collect.ImmutableSet;
import dev.hoot.api.entities.Players;
import dev.hoot.api.game.Skills;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import net.runelite.api.NpcID;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;

import java.util.Set;
import java.util.function.BooleanSupplier;

@AllArgsConstructor
@Getter
public enum Target {
    MASTER_FARMER(
            ImmutableSet.of(NpcID.MASTER_FARMER, NpcID.MASTER_FARMER_5731),
            ImmutableSet.of(
                    new Location(new WorldPoint(3082, 3249, 0), new WorldPoint(3092, 3245, 0), null),
                    new Location(new WorldPoint(1260, 3729, 0), new WorldPoint(1253, 3741, 0), null),
                    new Location(new WorldPoint(1249, 3748, 0), new WorldPoint(1249, 3748, 0), () -> Skills.getLevel(Skill.FARMING) >= 85)
            )
    );

    private final Set<Integer> ids;
    private final Set<Location> locations;

    public Location getNearest() {
        Location nearest = null;

        for (Location location : locations) {
            if (location.requirements != null && !location.requirements.getAsBoolean()) {
                continue;
            }

            if (nearest == null) {
                nearest = location;
                continue;
            }

            if (Players.getLocal().distanceTo(location.pickpocketLocation) < Players.getLocal().distanceTo(nearest.pickpocketLocation)) {
                nearest = location;
            }
        }

        return nearest;
    }

    @Value
    public static class Location {
        WorldPoint pickpocketLocation;
        WorldPoint bankLocation;
        BooleanSupplier requirements;
    }
}
