package io.reisub.unethicalite.chompychomper.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.NPCs;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.chompychomper.ChompyChomper;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;

public class Inflate extends Task {
  @Inject
  private ChompyChomper plugin;

  @Override
  public String getStatus() {
    return "Inflating toad";
  }

  @Override
  public boolean validate() {
    return plugin.getCurrentActivity() == Activity.IDLE
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

    GameThread.invoke(() -> toad.interact(0));
    Time.sleepTicksUntil(() -> Inventory.getCount(ItemID.BLOATED_TOAD) > toadCount, 20);
  }
}
