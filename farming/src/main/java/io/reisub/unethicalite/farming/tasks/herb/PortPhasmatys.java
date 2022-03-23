package io.reisub.unethicalite.farming.tasks.herb;

import dev.hoot.api.entities.Players;
import dev.hoot.api.items.Bank;
import dev.hoot.api.utils.MessageUtils;
import dev.hoot.bot.managers.Static;
import io.reisub.unethicalite.utils.api.CMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.coords.WorldPoint;

public class PortPhasmatys extends Task {
    private boolean done;

    private final WorldPoint patchPoint = new WorldPoint(0,0, 0);

    @Override
    public String getStatus() {
        return "Going to Port Phasmatys herb patch";
    }

    @Override
    public boolean validate() {
        return !done;
    }

    @Override
    public void execute() {
        Item ectophial = Bank.Inventory.getFirst(ItemID.ECTOPHIAL, ItemID.ECTOPHIAL_4252);
        if (ectophial == null) {
            done = true;
            MessageUtils.addMessage("No ectophial found, skipping Port Phasmatys patch");
            return;
        }

        ectophial.interact("Teleport"); // TODO check action name

        CMovement.walkTo(patchPoint);

        if (Players.getLocal().distanceTo(patchPoint) < 10) {
            done = true;
        }
    }
}
