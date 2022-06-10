package io.reisub.unethicalite.giantsfoundry.enums;

import java.awt.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.client.ui.ColorScheme;

@Getter
@AllArgsConstructor
public enum Stage {
  TRIP_HAMMER("Hammer", ColorScheme.PROGRESS_ERROR_COLOR, 20, -25, Heat.HIGH),
  GRINDSTONE("Grind", ColorScheme.PROGRESS_INPROGRESS_COLOR, 10, 15, Heat.MED),
  POLISHING_WHEEL("Polish", ColorScheme.PROGRESS_COMPLETE_COLOR, 10, -17, Heat.LOW);

  private final String name;
  private final Color color;
  private final int progressPerAction;
  private final int heatChange;
  private final Heat heatLevel;
}
