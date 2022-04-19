package io.reisub.unethicalite.chompychomper.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.NPCs;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.chompychomper.ChompyChomper;
import io.reisub.unethicalite.chompychomper.Config;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;

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
    return plugin.getCurrentActivity() == Activity.IDLE
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
