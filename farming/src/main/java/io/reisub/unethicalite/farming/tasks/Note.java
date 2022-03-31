package io.reisub.unethicalite.farming.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.NPCs;
import dev.hoot.api.game.GameThread;
import dev.hoot.api.items.Inventory;
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
