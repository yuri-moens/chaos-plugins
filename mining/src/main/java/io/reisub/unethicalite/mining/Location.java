package io.reisub.unethicalite.mining;

import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.coords.WorldPoint;

import java.util.LinkedList;
import java.util.Set;

@AllArgsConstructor
@Getter
public enum Location {
    QUARRY_SANDSTONE(
            new WorldPoint(3165, 2914, 0),
            ImmutableSet.of(11386, 11387),
            new LinkedList<>() {
                {
                    add(new RockPosition(new WorldPoint(3166, 2913, 0), new WorldPoint(3166, 2914, 0)));
                    add(new RockPosition(new WorldPoint(3164, 2915, 0), new WorldPoint(3165, 2915, 0)));
                    add(new RockPosition(new WorldPoint(3164, 2914, 0), new WorldPoint(3165, 2914, 0)));
                    add(new RockPosition(new WorldPoint(3167, 2913, 0), new WorldPoint(3166, 2914, 0)));
                }
            },
            true
    ),
    QUARRY_GRANITE(
            new WorldPoint(0, 0, 0),
            null,
            new LinkedList<>(),
            true
    );

    private final WorldPoint miningAreaPoint;
    private final Set<Integer> rockIds;
    private final LinkedList<RockPosition> rockPositions;
    private final boolean threeTick;
}
