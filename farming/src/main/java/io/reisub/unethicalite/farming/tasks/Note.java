package io.reisub.unethicalite.farming.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.NPCs;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.NPC;

public class Note extends Task {
  @Override
  public String getStatus() {
    return "Noting herbs";
  }

  @Override
  public boolean validate() {
    return Inventory.contains(Predicates.ids(Constants.GRIMY_HERB_IDS));
  }

  @Override
  public void execute() {
    Item herb = Inventory.getFirst(Predicates.ids(Constants.GRIMY_HERB_IDS));
    NPC leprechaun = NPCs.getNearest("Tool Leprechaun");
    if (herb == null || leprechaun == null) {
      return;
    }

    GameThread.invoke(() -> herb.useOn(leprechaun));
    Time.sleepTicksUntil(() -> !Inventory.contains(herb.getId()), 30);
  }
}
