package io.reisub.unethicalite.woodcutting;

import com.google.common.collect.ImmutableSet;
import java.util.ArrayDeque;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.coords.WorldPoint;

@RequiredArgsConstructor
@Getter
public enum Location {
  YEW_WOODCUTTING_GUILD(
      new WorldPoint(1593, 3487, 0),
      20,
      new WorldPoint(1591, 3477, 0),
      ImmutableSet.of(),
      new ArrayDeque<>() {
        {
          add(new WorldPoint(0,0,0));
        }
      },
      false
  ),
  ;

  private final WorldPoint woodcuttingAreaPoint;
  private final int woodcuttingAreaRadius;
  private final WorldPoint bankPoint;
  private final Set<Integer> treeIds;
  private final ArrayDeque<WorldPoint> treePositions;
  private final boolean ordered;
}
