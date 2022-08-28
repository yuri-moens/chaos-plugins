package io.reisub.unethicalite.chompychomper.tasks;

import io.reisub.unethicalite.chompychomper.ChompyChomper;
import io.reisub.unethicalite.utils.api.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;

public class Inflate extends Task {
  @Inject private ChompyChomper plugin;

  @Override
  public String getStatus() {
    return "Inflating toad";
  }

  @Override
  public boolean validate() {
    return plugin.isCurrentActivity(Activity.IDLE)
        && !Inventory.isFull()
        && Inventory.contains(Predicates.ids(ChompyChomper.FILLED_BELLOW_IDS));
  }

  @Override
  public void execute() {
    NPC toad = NPCs.getNearest(NpcID.SWAMP_TOAD);
    if (toad == null) {
      return;
    }

    int toadCount = Inventory.getCount(ItemID.BLOATED_TOAD);

    GameThread.invoke(() -> toad.interact("Inflate"));
    Time.sleepTicksUntil(() -> Inventory.getCount(ItemID.BLOATED_TOAD) > toadCount, 20);
  }
}
