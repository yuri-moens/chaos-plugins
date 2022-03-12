package io.reisub.unethicalite.birdhouse.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.NPCs;
import dev.hoot.api.game.GameThread;
import dev.hoot.api.items.Inventory;
import io.reisub.unethicalite.birdhouse.BirdHouse;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.AllArgsConstructor;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;

@AllArgsConstructor
public class NoteSeaweed extends Task {
    private final BirdHouse plugin;

    @Override
    public String getStatus() {
        return "Noting seaweed";
    }

    @Override
    public boolean validate() {
        return plugin.isUnderwater()
                && Inventory.contains(ItemID.GIANT_SEAWEED);
    }

    @Override
    public void execute() {
        Item seaweed = Inventory.getFirst(ItemID.GIANT_SEAWEED);
        NPC leprechaun = NPCs.getNearest("Tool Leprechaun");
        if (seaweed == null || leprechaun == null) {
            return;
        }

        GameThread.invoke(() -> seaweed.useOn(leprechaun));
        Time.sleepTicksUntil(() -> !Inventory.contains(ItemID.GIANT_SEAWEED), 30);
    }
}
