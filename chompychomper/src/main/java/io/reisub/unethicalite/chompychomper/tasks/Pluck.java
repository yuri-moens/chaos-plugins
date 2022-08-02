package io.reisub.unethicalite.chompychomper.tasks;

import io.reisub.unethicalite.chompychomper.ChompyChomper;
import io.reisub.unethicalite.chompychomper.Config;
import io.reisub.unethicalite.utils.api.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;

public class Pluck extends Task {
  @Inject private ChompyChomper plugin;

  @Inject private Config config;

  private NPC deadChompy;

  @Override
  public String getStatus() {
    return "Plucking chompy";
  }

  @Override
  public boolean validate() {
    return plugin.isCurrentActivity(Activity.IDLE)
        && config.pluck()
        && (deadChompy = NPCs.getNearest(NpcID.CHOMPY_BIRD_1476)) != null;
  }

  @Override
  public void execute() {
    int featherCount = Inventory.getCount(true, ItemID.FEATHER);

    GameThread.invoke(() -> deadChompy.interact(0));

    Time.sleepTicksUntil(() -> Inventory.getCount(true, ItemID.FEATHER) > featherCount, 15);
  }
}
