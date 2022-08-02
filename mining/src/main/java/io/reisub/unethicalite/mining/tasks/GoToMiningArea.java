package io.reisub.unethicalite.mining.tasks;

import io.reisub.unethicalite.mining.Config;
import io.reisub.unethicalite.mining.Mining;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;

public class GoToMiningArea extends Task {

  private static final int TRAHAEARN_MINE_REGION = 13250;
  private static final int SALT_MINE_REGION = 11425;

  @Inject private Mining plugin;
  @Inject private Config config;

  @Override
  public String getStatus() {
    return "Going to mining area";
  }

  @Override
  public boolean validate() {
    return Players.getLocal().distanceTo(config.location().getMiningAreaPoint())
            > config.location().getMiningAreaDistance()
        && !Inventory.isFull();
  }

  @Override
  public int execute() {
    if (!Movement.isRunEnabled()) {
      if (!config.location().isThreeTick()
          || (config.location().isThreeTick() && Movement.getRunEnergy() > 70)) {
        Movement.toggleRun();
      }
    }

    if (config.location().isThreeTick()) {
      ChaosMovement.walkTo(config.location().getMiningAreaPoint());

      Time.sleepTicksUntil(
          () ->
              Players.getLocal().getWorldLocation().equals(config.location().getMiningAreaPoint()),
          20);
      plugin.setArrived(true);
    } else {
      switch (config.location()) {
        case SOFT_CLAY:
          final TileObject caveEntrance = TileObjects.getNearest(ObjectID.CAVE_ENTRANCE_36556);

          if (caveEntrance != null) {
            caveEntrance.interact("Enter");

            Time.sleepTicksUntil(() -> Utils.isInRegion(TRAHAEARN_MINE_REGION), 40);
            Time.sleepTick();

            final WorldPoint destination = config.location().getMiningAreaPoint();

            Movement.walk(destination);

            if (Inventory.contains(ItemID.BRACELET_OF_CLAY)) {
              Time.sleepTick();
              Inventory.getFirst(ItemID.BRACELET_OF_CLAY).interact("Wear");
            }

            Time.sleepTicksUntil(() -> Players.getLocal().distanceTo(destination) < 3, 15);
          }
          break;
        case BASALT:
          final TileObject caveEntrance2 = TileObjects.getNearest(ObjectID.STAIRS_33234);

          if (caveEntrance2 != null) {
            caveEntrance2.interact("Descend");

            Time.sleepTicksUntil(() -> Utils.isInRegion(SALT_MINE_REGION), 20);
            Time.sleepTick();

            final WorldPoint destination = config.location().getMiningAreaPoint();

            Movement.walk(destination);

            Time.sleepTicksUntil(() -> Players.getLocal().distanceTo(destination) < 6, 15);
          }
          break;
        default:
          ChaosMovement.walkTo(config.location().getMiningAreaPoint(), 2);
          break;
      }
    }

    return 1;
  }
}
