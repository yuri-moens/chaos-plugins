package io.reisub.unethicalite.alchemicalhydrafighter.tasks;

import io.reisub.unethicalite.alchemicalhydra.entity.HydraPhase;
import io.reisub.unethicalite.alchemicalhydrafighter.AlchemicalHydraFighter;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.entities.Players;

public class DodgeLightning extends Task {

  @Inject
  private AlchemicalHydraFighter plugin;

  @Override
  public String getStatus() {
    return "Dodging lightning";
  }

  @Override
  public boolean validate() {
    return plugin.getPhase() == HydraPhase.LIGHTNING
        && plugin.getHydraPlugin().getHydra().getNextSpecial() == 0;
  }

  @Override
  public void execute() {
    WorldPoint greenVent = plugin.getGreenVentLocation();
    WorldPoint blueVent = plugin.getBlueVentLocation();

    if (Players.getLocal().getWorldLocation().getX() > blueVent.getX()) {
      // first lightning
      plugin.walk(greenVent.dx(-1).dy(6)); // walk to top near green vent
      plugin.walk(blueVent.dx(-4).dy(6)); // walk to top left corner
      plugin.walk(blueVent.dx(-6).dy(2)); // walk to the left wall
      plugin.walk(blueVent.dx(-6).dy(-4)); // walk 6 tiles down the left wall
      plugin.walk(blueVent.dx(-6).dy(2)); // walk back up
      plugin.walk(blueVent.dx(-3).dy(1)); // walk to attack position
    } else {
      // second lightning, most likely get hit by one
      plugin.walk(blueVent.dx(-6).dy(4)); // walk to top left corner
      plugin.walk(blueVent.dx(-3).dy(1)); // walk to attack position
    }
  }
}
