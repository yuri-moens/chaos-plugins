package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;


public class MoveToMainArea extends Task {
  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Going to main area";
  }

  @Override
  public boolean validate() {
    return plugin.getGamePhase() == 5;
  }

  @Override
  public void execute() {
    TileObjects.getNearest("Rubble").interact("Climb");
    Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().getWorldX() < 3633, 10);
    Time.sleepTicks(2);
    plugin.setGamePhase(10);

  }
}