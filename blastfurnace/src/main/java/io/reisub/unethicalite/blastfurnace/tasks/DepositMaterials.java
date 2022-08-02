package io.reisub.unethicalite.blastfurnace.tasks;

import io.reisub.unethicalite.blastfurnace.BlastFurnace;
import io.reisub.unethicalite.blastfurnace.Config;
import io.reisub.unethicalite.utils.api.Activity;
import io.reisub.unethicalite.utils.enums.Metal;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Rand;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;

public class DepositMaterials extends Task {
  private static final String CONVEYER_BELT_ACTION = "Put-ore-on";
  @Inject private BlastFurnace plugin;
  @Inject private Config config;

  @Override
  public String getStatus() {
    return "Depositing materials";
  }

  @Override
  public boolean validate() {
    return plugin.isCurrentActivity(Activity.IDLE)
        && Inventory.isFull()
        && !Inventory.contains(config.metal().getBarId());
  }

  @Override
  public void execute() {
    plugin.setActivity(Activity.DEPOSITING);

    if (!config.useStamina() && Movement.isRunEnabled()) {
      Movement.toggleRun();
    }

    final TileObject conveyorBelt = TileObjects.getNearest(ObjectID.CONVEYOR_BELT);
    if (conveyorBelt == null) {
      return;
    }

    final Item coalBag = Inventory.getFirst(ItemID.COAL_BAG_12019);

    if (config.metal() == Metal.GOLD) {
      if (!Equipment.contains(ItemID.GOLDSMITH_GAUNTLETS)) {
        conveyorBelt.interact(CONVEYER_BELT_ACTION);
        Time.sleepTicks(Rand.nextInt(4, 7));

        final Item gauntlets = Inventory.getFirst(ItemID.GOLDSMITH_GAUNTLETS);
        if (gauntlets != null) {
          gauntlets.interact("Wear");
        }
      }

      conveyorBelt.interact(CONVEYER_BELT_ACTION);
      Time.sleepTicksUntil(() -> plugin.isCurrentActivity(Activity.IDLE), 25);
    } else {
      conveyorBelt.interact(CONVEYER_BELT_ACTION);
      Time.sleepTicksUntil(() -> plugin.isCurrentActivity(Activity.IDLE), 25);

      plugin.setActivity(Activity.DEPOSITING);

      coalBag.interact("Empty");
      Time.sleepTick();

      boolean successful = false;

      while (!successful) {
        conveyorBelt.interact(CONVEYER_BELT_ACTION);
        successful = Time.sleepTicksUntil(() -> plugin.isCurrentActivity(Activity.IDLE), 3);
      }
    }

    if (!Movement.isRunEnabled()) {
      Movement.toggleRun();
    }

    if (plugin.isExpectingBars()) {
      Movement.walk(new WorldPoint(1940, 4962, 0));
    }
  }
}
