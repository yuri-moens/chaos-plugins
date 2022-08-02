package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.items.Inventory;


public class PowerGuardian extends Task {
  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Powering Guardian";
  }

  @Override
  public boolean validate() {
    return plugin.getGamePhase() == 10
        &&
        Inventory.contains("Elemental guardian stone", "Catalytic guardian stone");
  }

  @Override
  public int execute() {
    NPCs.getNearest("The Great Guardian").interact("Power-up");
    Time.sleepTicksUntil(
        () -> !Inventory.contains("Elemental guardian stone", "Catalytic guardian stone"), 10);

    return 1;
  }
}