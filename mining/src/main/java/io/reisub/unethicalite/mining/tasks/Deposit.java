package io.reisub.unethicalite.mining.tasks;

import com.google.common.collect.ImmutableSet;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.Set;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;

public class Deposit extends Task {
  private static final Set<Integer> SANDSTONE_IDS =
      ImmutableSet.of(
          ItemID.SANDSTONE_1KG,
          ItemID.SANDSTONE_2KG,
          ItemID.SANDSTONE_5KG,
          ItemID.SANDSTONE_10KG,
          ItemID.SANDSTONE_20KG,
          ItemID.SANDSTONE_32KG);

  private boolean interrupted;

  @Override
  public String getStatus() {
    return "Depositing";
  }

  @Override
  public boolean validate() {
    return Inventory.isFull() && Inventory.contains(Predicates.ids(SANDSTONE_IDS));
  }

  @Override
  public void execute() {
    interrupted = false;

    if (Movement.isRunEnabled()) {
      Movement.toggleRun();
    }

    NPC drew = NPCs.getNearest(NpcID.DREW);
    if (drew == null) {
      return;
    }

    GameThread.invoke(() -> Inventory.getFirst(Predicates.ids(SANDSTONE_IDS)).useOn(drew));
    Time.sleepTicksUntil(() -> !Inventory.isFull() || interrupted, 30);
  }

  @Subscribe
  private void onChatMessage(ChatMessage event) {
    if (event.getMessage().equals("You take a drink of water.")) {
      interrupted = true;
    }
  }
}
