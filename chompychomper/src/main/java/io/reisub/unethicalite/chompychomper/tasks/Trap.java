package io.reisub.unethicalite.chompychomper.tasks;

import com.google.common.collect.ImmutableSet;
import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.NPCs;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.movement.Movement;
import io.reisub.unethicalite.chompychomper.ChompyChomper;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.coords.WorldPoint;

public class Trap extends Task {
  @Inject
  private ChompyChomper plugin;

  private final Set<WorldPoint> TRAP_LOCATIONS = ImmutableSet.of(
      new WorldPoint(2341, 3063, 0),
      new WorldPoint(2340, 3062, 0),
      new WorldPoint(2339, 3061, 0),
      new WorldPoint(2339, 3060, 0),
      new WorldPoint(2336, 3060, 0),
      new WorldPoint(2336, 3059, 0),
      new WorldPoint(2336, 3058, 0),
      new WorldPoint(2336, 3057, 0),
      new WorldPoint(2336, 3056, 0)
  );

  @Override
  public String getStatus() {
    return "Setting toad traps";
  }

  @Override
  public boolean validate() {
    return plugin.getCurrentActivity() == Activity.IDLE
        && Inventory.getCount(ItemID.BLOATED_TOAD) == 3;
  }

  @Override
  public void execute() {
    WorldPoint nearest = findNearestEmptyTrapLocation();
    if (nearest == null) {
      return;
    }

    Movement.walk(nearest);
    Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().equals(nearest), 20);

    Inventory.getAll(ItemID.BLOATED_TOAD).forEach((i) -> {
      i.interact(0);
      Time.sleepTicks(4);
    });
  }

  private WorldPoint findNearestEmptyTrapLocation() {
    WorldPoint nearest = null;
    List<NPC> bloatedToads = NPCs.getAll(NpcID.BLOATED_TOAD);

    for (WorldPoint trapLocation : TRAP_LOCATIONS) {
      boolean empty = true;

      for (int i = 0; i < 3; i++) {
        WorldPoint wp = trapLocation.dx(-i);

        for (NPC bloatedToad : bloatedToads) {
          if (bloatedToad.getWorldLocation().equals(wp)) {
            empty = false;
            break;
          }
        }
      }

      if (empty && (nearest == null || Players.getLocal().distanceTo(trapLocation) < Players.getLocal().distanceTo(nearest))) {
        nearest = trapLocation;
      }
    }

    return nearest;
  }
}
