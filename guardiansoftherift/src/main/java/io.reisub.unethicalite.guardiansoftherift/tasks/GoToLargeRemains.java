package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.guardiansoftherift.data.GotrArea;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;

public class GoToLargeRemains extends Task {

  private static final WorldPoint END_OF_RUBBLE = new WorldPoint(3637, 9503, 0);
  private static final WorldPoint NEAR_REMAINS = new WorldPoint(3639, 9500, 0);
  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Moving to the large remains";
  }

  @Override
  public boolean validate() {
    return plugin.getElapsedTicks() < 200
        && !Inventory.contains(ItemID.GUARDIAN_FRAGMENTS)
        && GotrArea.getCurrent() == GotrArea.MAIN;
  }

  @Override
  public void execute() {
    TileObjects.getNearest("Rubble").interact("Climb");

    Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().equals(END_OF_RUBBLE), 30);
    Time.sleepTicks(3);

    Movement.walk(NEAR_REMAINS);
    Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().equals(NEAR_REMAINS), 5);
  }
}