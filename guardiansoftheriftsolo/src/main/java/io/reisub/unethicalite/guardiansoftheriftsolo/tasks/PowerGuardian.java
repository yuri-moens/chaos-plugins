package io.reisub.unethicalite.guardiansoftheriftsolo.tasks;

import io.reisub.unethicalite.guardiansoftheriftsolo.GuardiansOfTheRiftSolo;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.AreaType;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Inventory;

public class PowerGuardian extends Task {

  @Inject
  private GuardiansOfTheRiftSolo plugin;
  @Inject
  private EnterAltar enterAltarTask;

  @Override
  public String getStatus() {
    return "Powering up guardian";
  }

  @Override
  public boolean validate() {
    return plugin.isGameActive()
        && AreaType.getCurrent() == AreaType.MAIN
        && Inventory.contains(ItemID.ELEMENTAL_GUARDIAN_STONE, ItemID.CATALYTIC_GUARDIAN_STONE);
  }

  @Override
  public void execute() {
    final NPC guardian = NPCs.getNearest(NpcID.THE_GREAT_GUARDIAN);

    if (guardian == null) {
      return;
    }

    guardian.interact("Power-up");
    if (!Time.sleepTicksUntil(() -> Players.getLocal().isMoving(), 2)) {
      return;
    }

    Time.sleepTicksUntil(
        () -> !Inventory.contains(ItemID.ELEMENTAL_GUARDIAN_STONE, ItemID.CATALYTIC_GUARDIAN_STONE),
        20
    );

    if (plugin.getGuardianPower() < 100) {
      enterAltarTask.execute();
    }
  }
}
