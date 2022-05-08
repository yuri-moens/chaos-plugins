package io.reisub.unethicalite.pyramidplunder.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.NPCs;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.movement.Reachable;
import io.reisub.unethicalite.pyramidplunder.PyramidPlunder;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.ArrayDeque;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;

public class GoToPyramid extends Task {

  private static final ArrayDeque<Integer> DOOR_IDS = new ArrayDeque<>(4);

  private int lastCorrectDoor;

  @Override
  public String getStatus() {
    return "Going to pyramid";
  }

  @Override
  public boolean validate() {
    return !Utils.isInRegion(PyramidPlunder.PYRAMID_PLUNDER_REGION)
        && Inventory.getFreeSlots() > 14;
  }

  @Override
  public void execute() {
    if (Utils.isInRegion(PyramidPlunder.SOPHANEM_BANK_REGION)) {
      final TileObject ladder = TileObjects.getNearest(ObjectID.LADDER_20277);
      if (ladder == null) {
        return;
      }

      ladder.interact(0);

      if (Inventory.contains(ItemID.LOBSTER)) {
        Time.sleepTick();
        Inventory.getFirst(ItemID.LOBSTER).interact(1);
        Time.sleepTick();
        ladder.interact(0);
      }

      Time.sleepTicksUntil(() -> Utils.isInRegion(PyramidPlunder.SOPHANEM_REGION), 15);
      Time.sleepTick();
    }

    final TileObject door = getDoor();
    if (door == null) {
      return;
    }

    door.interact(0);

    if (Inventory.contains(ItemID.LOBSTER)) {
      Time.sleepTick();

      Inventory.getAll(ItemID.LOBSTER).forEach(i -> {
        i.interact(1);
        Time.sleepTicks(3);
      });

      door.interact(0);
    }

    Time.sleepTicksUntil(() -> Utils.isInRegion(PyramidPlunder.PYRAMID_PLUNDER_REGION)
        || Players.getLocal().getModelHeight() == 1000, 50);

    if (Players.getLocal().getModelHeight() == 1000) {
      Time.sleepTicksUntil(() -> Players.getLocal().getModelHeight() != 1000, 15);

      door.interact(0);
      Time.sleepTicksUntil(() -> Utils.isInRegion(PyramidPlunder.PYRAMID_PLUNDER_REGION), 15);
    }

    final NPC mummy = NPCs.getNearest(NpcID.GUARDIAN_MUMMY);

    if (mummy != null && Reachable.isInteractable(mummy)) {
      lastCorrectDoor = door.getId();
      resetQueue();
    } else {
      lastCorrectDoor = 0;
      DOOR_IDS.remove(door.getId());
    }
  }

  private TileObject getDoor() {
    if (lastCorrectDoor != 0) {
      return TileObjects.getNearest(lastCorrectDoor);
    }

    if (DOOR_IDS.isEmpty()) {
      resetQueue();
    }

    return TileObjects.getNearest(DOOR_IDS.poll());
  }

  private void resetQueue() {
    DOOR_IDS.clear();
    DOOR_IDS.add(26622);
    DOOR_IDS.add(26623);
    DOOR_IDS.add(26625);
    DOOR_IDS.add(26624);
  }
}
