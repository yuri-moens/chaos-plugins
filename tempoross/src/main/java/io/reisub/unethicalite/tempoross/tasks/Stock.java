package io.reisub.unethicalite.tempoross.tasks;

import io.reisub.unethicalite.tempoross.Tempoross;
import io.reisub.unethicalite.tempoross.data.PluginActivity;
import io.reisub.unethicalite.utils.api.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.NullObjectID;
import net.unethicalite.api.commons.Rand;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;

public class Stock extends Task {
  @Inject private Tempoross plugin;

  @Override
  public String getStatus() {
    return "Stocking cannon";
  }

  @Override
  public boolean validate() {
    if (!plugin.isInTemporossArea()) {
      return false;
    }

    if (plugin.isCurrentActivity(PluginActivity.REPAIRING)
        || plugin.isCurrentActivity(PluginActivity.TETHERING_MAST)) {
      return false;
    }

    if (TileObjects.getNearest(
            (o) ->
                o.getId() == NullObjectID.NULL_41006
                    && (plugin.getIslandArea().contains(o) || plugin.getBoatArea().contains(o)))
        != null) {
      return false;
    }

    // stock cannon for the first time
    // bring energy to 4%
    if (plugin.getPhase() == 1
        && plugin.getCookedFish() >= plugin.getCookedFishRequired()
        && plugin.getEnergy() == 100
        && plugin.isCurrentActivity(PluginActivity.STOCKING_CANNON)) {
      return true;
    }

    // stock cannon for the second time
    // trigger first phase change
    // bring energy to 4% or as close as possible
    if (plugin.getPhase() == 1
        && plugin.getEnergy() < 100
        && (plugin.getStormIntensity() >= 94
            || plugin.getCookedFish() >= plugin.getCookedFishRequired())
        && plugin.isCurrentActivity(PluginActivity.STOCKING_CANNON)) {
      return true;
    }

    // stock cannon at phase 2
    // bring energy to 4%
    if (plugin.getPhase() == 2
        && plugin.getEnergy() != 4
        && plugin.getCookedFishRequired() > 0
        && plugin.getCookedFishRequired() != 19
        && plugin.getCookedFish() >= plugin.getCookedFishRequired()
        && plugin.isCurrentActivity(PluginActivity.STOCKING_CANNON)) {
      return true;
    }

    // stock cannon at phase 2
    // trigger second phase change
    // bring energy to 4%
    if (plugin.getPhase() == 2
        && plugin.getCookedFish() >= plugin.getCookedFishRequired()
        && plugin.isCurrentActivity(PluginActivity.STOCKING_CANNON)) {
      return true;
    }

    // stock cannon at phase 3
    // trigger third phase change and potentially fourth phase change
    if (plugin.getPhase() >= 3
        && plugin.getCookedFish() > 0
        && plugin.getRawFish() == 0
        && Inventory.isFull()
        && plugin.isCurrentActivity(PluginActivity.STOCKING_CANNON)) {
      return true;
    }

    NPC northCrate = NPCs.getNearest(NpcID.AMMUNITION_CRATE);
    NPC southCrate = NPCs.getNearest(NpcID.AMMUNITION_CRATE_10577);

    if (northCrate != null && southCrate != null) {
      // swap ammunition box at phase 3
      if (plugin.getPhase() >= 4
          && plugin.isCurrentActivity(PluginActivity.STOCKING_CANNON)
          && plugin.getCookedFish() > 0
          && plugin.getCookedFish() < 15
          && Players.getLocal().distanceTo(northCrate)
              < Players.getLocal().distanceTo(southCrate)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void execute() {
    if (Players.getLocal().distanceTo(plugin.getDudiPos()) > 8) {
      Movement.walk(plugin.getDudiPos().dx(Rand.nextInt(-2, 3)).dy(Rand.nextInt(-2, 3)));

      Time.sleepUntil(() -> Players.getLocal().distanceTo(plugin.getDudiPos()) <= 8, 15000);
    }

    NPC fire =
        NPCs.getNearest(
            (n) ->
                n.getId() == NpcID.FIRE_8643
                    && (plugin.getIslandArea().contains(n) || plugin.getBoatArea().contains(n)));
    if (fire != null) {
      return;
    }

    NPC crate;

    if (plugin.getPhase() >= 4 && plugin.isCurrentActivity(PluginActivity.STOCKING_CANNON)) {
      plugin.setActivity(Activity.IDLE);
      crate = NPCs.getNearest(NpcID.AMMUNITION_CRATE_10577);
    } else {
      if (NPCs.getNearest(NpcID.CANNONEER).getAnimation() == 7211) {
        crate = NPCs.getNearest(NpcID.AMMUNITION_CRATE_10577);
      } else {
        crate = NPCs.getNearest(NpcID.AMMUNITION_CRATE);
      }
    }

    if (crate == null) {
      return;
    }

    crate.interact(0);

    Time.sleepTicksUntil(() -> plugin.isCurrentActivity(PluginActivity.STOCKING_CANNON), 3);
  }
}
