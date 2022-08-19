package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.guardiansoftherift.data.GotrArea;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;

public class LeaveAltar extends Task {

  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Leaving altar";
  }

  @Override
  public boolean validate() {
    return GotrArea.getCurrent() == GotrArea.ALTAR
        && !Inventory.contains("Guardian essence");
  }

  @Override
  public void execute() {
    TileObjects.getNearest("Portal").interact("Use");
    Time.sleepTicksUntil(() -> GotrArea.getCurrent() == GotrArea.MAIN, 20);
    Time.sleepTick();

    if (Inventory.isFull()) {
      Movement.walk(new WorldPoint(3615, 9509, 0));
    }
  }
}