package io.reisub.unethicalite.mining;

import lombok.Value;
import net.runelite.api.coords.WorldPoint;

@Value
public class RockPosition {
  WorldPoint rock;
  WorldPoint interactFrom;
}
