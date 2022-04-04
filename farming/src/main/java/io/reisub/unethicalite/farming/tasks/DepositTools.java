package io.reisub.unethicalite.farming.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.NPCs;
import dev.hoot.api.game.GameThread;
import dev.hoot.api.items.Equipment;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.widgets.Widgets;
import io.reisub.unethicalite.farming.Farming;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;

import javax.inject.Inject;

public class DepositTools extends Task {
    @Inject
    private Farming plugin;

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

        if (Inventory.contains(ItemID.BOTTOMLESS_COMPOST_BUCKET, ItemID.BOTTOMLESS_COMPOST_BUCKET_22997)) {
            Constants.TOOLS_DEPOSIT_BOTTOMLESS_BUCKET_WIDGET.get().interact(0);
        }

        Constants.TOOLS_CLOSE_WIDGET.get().interact("Close");

        plugin.stop("Finished farm run. Stopping plugin.");
    }
}
