package io.reisub.unethicalite.woodcutting;

import com.google.common.collect.ImmutableSet;
import java.util.ArrayDeque;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ObjectID;
import net.runelite.api.coords.WorldPoint;

@RequiredArgsConstructor
@Getter
public enum Location {
  YEW_WOODCUTTING_GUILD(
      new WorldPoint(1593, 3487, 0),
      20,
      new WorldPoint(1591, 3477, 0),
      ImmutableSet.of(
          ObjectID.YEW_TREE,
          ObjectID.YEW_TREE_5121,
          ObjectID.YEW_TREE_8503,
          ObjectID.YEW_TREE_8504,
          ObjectID.YEW_TREE_8505,
          ObjectID.YEW_TREE_8506,
          ObjectID.YEW_TREE_8507,
          ObjectID.YEW_TREE_8508,
          ObjectID.YEW_TREE_8509,
          ObjectID.YEW_TREE_8510,
          ObjectID.YEW_TREE_8511,
          ObjectID.YEW_TREE_8512,
          ObjectID.YEW_TREE_8513,
          ObjectID.YEW
      ),
      -1,
      -1,
      new ArrayDeque<>() {
        {
          add(new WorldPoint(1596,3485,0));
          add(new WorldPoint(1591,3487,0));
          add(new WorldPoint(1596,3490,0));
          add(new WorldPoint(1591,3493,0));
          add(new WorldPoint(1596,3495,0));
        }
      },
      false
  ),
  ;

  private final WorldPoint woodcuttingAreaPoint;
  private final int woodcuttingAreaRadius;
  private final WorldPoint bankPoint;
  private final Set<Integer> treeIds;
  private final int xoffset;
  private final int yoffset;
  private final ArrayDeque<WorldPoint> treePositions;
  private final boolean ordered;
}
