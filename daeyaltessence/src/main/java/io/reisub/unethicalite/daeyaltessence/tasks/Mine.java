package io.reisub.unethicalite.daeyaltessence.tasks;

import io.reisub.unethicalite.daeyaltessence.DaeyaltEssence;
import io.reisub.unethicalite.daeyaltessence.data.Rock;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.input.naturalmouse.util.Pair;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;

@RequiredArgsConstructor
public class Mine extends Task {
  private final DaeyaltEssence plugin;

  @Override
  public String getStatus() {
    return "Mining";
  }

  @Override
  public boolean validate() {
    return Players.getLocal().getWorldLocation().getRegionID()
            == DaeyaltEssence.ESSENCE_MINE_REGION;
  }

  @Override
  public void execute() {
    if (!Movement.isRunEnabled()) {
      Movement.toggleRun();
    }

    final Rock rock = Rock.getActiveRock();
    final Item knife = Inventory.getFirst(ItemID.KNIFE);
    final Item logs = Inventory.getFirst(ItemID.TEAK_LOGS, ItemID.MAHOGANY_LOGS);

    if (rock == null || knife == null || logs == null) {
      System.out.println("rock: " + rock);
      return;
    }

    final Pair<WorldPoint, WorldPoint> nearest = rock.getNearestPoints();

    if (Players.getLocal().distanceTo(nearest.x) > 5) {
      Movement.walk(nearest.x);
      Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().equals(nearest.x), 15);
    }

    knife.useOn(logs);

    final WorldPoint target = Players.getLocal().getWorldLocation().equals(nearest.x)
        ? nearest.y
        : nearest.x;

    Movement.walk(target);
    Time.sleepTick();

    rock.getObject().interact("Mine");
    Time.sleepTicks(2);
  }
}
