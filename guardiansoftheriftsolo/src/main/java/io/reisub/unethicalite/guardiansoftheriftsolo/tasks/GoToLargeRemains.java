package io.reisub.unethicalite.guardiansoftheriftsolo.tasks;

import io.reisub.unethicalite.guardiansoftheriftsolo.GuardiansOfTheRiftSolo;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.AreaType;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;

public class GoToLargeRemains extends Task {

  private static final WorldPoint END_OF_RUBBLE = new WorldPoint(3637, 9503, 0);

  @Inject
  private GuardiansOfTheRiftSolo plugin;

  @Override
  public String getStatus() {
    return "Going to large guardian remains";
  }

  @Override
  public boolean validate() {
    return !plugin.isGameActive()
        && AreaType.getCurrent() == AreaType.MAIN;
  }

  @Override
  public void execute() {
    final TileObject rubble = TileObjects.getNearest(ObjectID.RUBBLE_43724);

    if (rubble == null) {
      return;
    }

    rubble.interact("Climb");
    Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().equals(END_OF_RUBBLE), 30);
    Time.sleepTicks(4);
  }
}
