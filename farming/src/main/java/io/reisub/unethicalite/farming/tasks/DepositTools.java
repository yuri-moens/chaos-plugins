package io.reisub.unethicalite.farming.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.NPCs;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.items.Equipment;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.widgets.Widgets;
import io.reisub.unethicalite.farming.Farming;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;

public class DepositTools extends Task {
  @Inject private Farming plugin;

  @Override
  public String getStatus() {
    return "Depositing tools";
  }

  @Override
  public boolean validate() {
    NPC leprechaun = NPCs.getNearest("Tool Leprechaun");

    return plugin.getLocationQueue().isEmpty()
        && (plugin.getCurrentLocation() == null || plugin.getCurrentLocation().isDone())
        && Inventory.contains(ItemID.SEED_DIBBER)
        && leprechaun != null;
  }

  @Override
  public void execute() {
    if (Equipment.contains(ItemID.MAGIC_SECATEURS)) {
      Equipment.getFirst(ItemID.MAGIC_SECATEURS).interact(0);
    }

    NPC leprechaun = NPCs.getNearest("Tool Leprechaun");

    GameThread.invoke(() -> leprechaun.interact("Exchange"));
    Time.sleepTicksUntil(() -> Widgets.isVisible(Constants.TOOLS_WIDGET.get()), 30);

    Constants.TOOLS_DEPOSIT_SECATEURS_WIDGET.get().interact(0);
    Constants.TOOLS_DEPOSIT_DIBBER_WIDGET.get().interact(0);
    Constants.TOOLS_DEPOSIT_SPADE_WIDGET.get().interact(0);

    if (Inventory.contains(ItemID.BUCKET)) {
      Constants.TOOLS_DEPOSIT_BUCKET_WIDGET.get().interact("Store-5");
    }

    if (Inventory.contains(
        ItemID.BOTTOMLESS_COMPOST_BUCKET, ItemID.BOTTOMLESS_COMPOST_BUCKET_22997)) {
      Constants.TOOLS_DEPOSIT_BOTTOMLESS_BUCKET_WIDGET.get().interact(0);
    }

    Constants.TOOLS_CLOSE_WIDGET.get().interact("Close");

    Item cape = Inventory.getFirst(Predicates.ids(Constants.CRAFTING_CAPE_IDS));
    if (cape != null) {
      cape.interact("Teleport");
    }

    plugin.stop("Finished farm run. Stopping plugin.");
  }
}
