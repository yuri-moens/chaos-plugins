package io.reisub.unethicalite.tempoross.tasks;

import io.reisub.unethicalite.tempoross.Tempoross;
import io.reisub.unethicalite.tempoross.data.PluginActivity;
import io.reisub.unethicalite.utils.api.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.items.Inventory;

public class DouseFire extends Task {
  @Inject private Tempoross plugin;

  private NPC fire;

  @Override
  public String getStatus() {
    return "Dousing fire";
  }

  @Override
  public boolean validate() {
    if (!plugin.isInTemporossArea()) {
      return false;
    }

    // don't douse flames when we're still getting the first 17 fish cooked
    if (plugin.getPhase() == 1
        && plugin.getRawFish() + plugin.getCookedFish() >= 17
        && plugin.getCookedFish() < 17
        && plugin.getCookedFish() > 10) {
      return false;
    }

    if (plugin.isCurrentActivity(PluginActivity.STOCKING_CANNON)
        && plugin.getCookedFishRequired() <= 10) {
      return false;
    }

    fire =
        NPCs.getNearest(
            (n) ->
                n.getId() == NpcID.FIRE_8643
                    && (plugin.getIslandArea().contains(n) || plugin.getBoatArea().contains(n)));

    return Inventory.contains(ItemID.BUCKET_OF_WATER) && fire != null;
  }

  @Override
  public void execute() {
    fire.interact(0);

    if (!Time.sleepUntil(() -> plugin.isCurrentActivity(PluginActivity.DOUSING_FIRE), 2500)) {
      return;
    }

    Time.sleepUntil(() -> plugin.isCurrentActivity(Activity.IDLE), 15000);
  }
}
