package io.reisub.unethicalite.mining.tasks;

import com.google.common.collect.ImmutableSet;
import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.NPCs;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.movement.Movement;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.eventbus.Subscribe;

import java.util.Set;

public class Deposit extends Task {
    private final Set<Integer> SANDSTONE_IDS = ImmutableSet.of(
            ItemID.SANDSTONE_1KG,
            ItemID.SANDSTONE_2KG,
            ItemID.SANDSTONE_5KG,
            ItemID.SANDSTONE_10KG,
            ItemID.SANDSTONE_20KG,
            ItemID.SANDSTONE_32KG
    );

    private boolean interrupted;

    @Override
    public String getStatus() {
        return "Depositing";
    }

    @Override
    public boolean validate() {
        return Inventory.isFull()
                && Inventory.contains(Predicates.ids(SANDSTONE_IDS));
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

        Inventory.getFirst(Predicates.ids(SANDSTONE_IDS)).useOn(drew);
        Time.sleepTicksUntil(() -> !Inventory.isFull() || interrupted, 30);
    }

    @Subscribe
    private void onChatMessage(ChatMessage event) {
        if (event.getMessage().equals("You take a drink of water.")) {
            interrupted = true;
        }
    }
}
