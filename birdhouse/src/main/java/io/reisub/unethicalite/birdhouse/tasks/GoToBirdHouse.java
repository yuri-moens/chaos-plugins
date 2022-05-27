package io.reisub.unethicalite.birdhouse.tasks;

import io.reisub.unethicalite.birdhouse.BirdHouse;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.AllArgsConstructor;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.entities.Players;

@AllArgsConstructor
public class GoToBirdHouse extends Task {
  private final BirdHouse plugin;
  private final WorldPoint target = new WorldPoint(3681, 3819, 0);

  @Override
  public String getStatus() {
    return "Going to southern bird house";
  }

  @Override
  public boolean validate() {
    return plugin.isEmptied(Constants.MEADOW_NORTH_SPACE)
        && !plugin.isEmptied(Constants.MEADOW_SOUTH_SPACE)
        && Players.getLocal().distanceTo(target) > 15;
  }

  @Override
  public void execute() {
    ChaosMovement.walkTo(target, 2);
  }
}
