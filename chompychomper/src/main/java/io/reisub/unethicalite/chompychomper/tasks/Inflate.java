package io.reisub.unethicalite.chompychomper.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.NPCs;
import dev.hoot.api.game.GameThread;
import dev.hoot.api.items.Inventory;
import io.reisub.unethicalite.chompychomper.ChompyChomper;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;

import javax.inject.Inject;

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
