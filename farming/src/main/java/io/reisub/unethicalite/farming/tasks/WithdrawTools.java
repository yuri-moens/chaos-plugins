package io.reisub.unethicalite.farming.tasks;

import io.reisub.unethicalite.farming.Farming;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Widgets;

public class WithdrawTools extends Task {
  @Inject private Farming plugin;

  @Override
  public String getStatus() {
    return "Withdrawing tools";
  }

  @Override
  public boolean validate() {
    NPC leprechaun = NPCs.getNearest("Tool Leprechaun");

    return !plugin.getLocationQueue().isEmpty()
        && !Inventory.contains(ItemID.SEED_DIBBER)
        && leprechaun != null;
  }

  @Override
  public int execute() {
    NPC leprechaun = NPCs.getNearest("Tool Leprechaun");

    GameThread.invoke(() -> leprechaun.interact("Exchange"));
    Time.sleepTicksUntil(() -> Widgets.isVisible(Constants.TOOLS_WIDGET.get()), 30);
    Time.sleepTick();

    Constants.TOOLS_WITHDRAW_SECATEURS_WIDGET.get().interact(0);
    Constants.TOOLS_WITHDRAW_DIBBER_WIDGET.get().interact(0);
    Constants.TOOLS_WITHDRAW_SPADE_WIDGET.get().interact(0);
    // TODO support other compost buckets
    Constants.TOOLS_WITHDRAW_BOTTOMLESS_BUCKET_WIDGET.get().interact(0);

    Constants.TOOLS_CLOSE_WIDGET.get().interact("Close");
    Time.sleepTicksUntil(() -> Inventory.contains(ItemID.SEED_DIBBER), 5);

    if (Inventory.contains(ItemID.MAGIC_SECATEURS)) {
      Inventory.getFirst(ItemID.MAGIC_SECATEURS).interact("Wield");
    }

    return 1;
  }
}
