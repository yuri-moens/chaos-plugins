package io.reisub.unethicalite.tempoross.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.NPCs;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.packets.NPCPackets;
import dev.hoot.api.packets.TileObjectPackets;
import dev.hoot.api.widgets.Dialog;
import io.reisub.unethicalite.tempoross.Tempoross;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.*;

import javax.inject.Inject;

public class LeaveGame extends Task {
    @Inject
    private Tempoross plugin;

    @Override
    public String getStatus() {
        return "Leaving game";
    }

    @Override
    public boolean validate() {
        return plugin.isFinished();
    }

    @Override
    public void execute() {
        TileObject buckets = TileObjects.getNearest(ObjectID.BUCKETS_40966);
        if (buckets == null) return;

        TileObjectPackets.tileObjectSecondOption(buckets, false);
        Time.sleepUntil(() -> Inventory.contains(ItemID.BUCKET), 100, 10000);

        NPC deri = NPCs.getNearest(NpcID.FIRST_MATE_DERI_10595);
        if (deri == null) return;

        NPCPackets.npcAction(deri, "Leave", false);;
        Time.sleepUntil(() -> plugin.isInDesert(), 20000);

        Time.sleepUntil(Dialog::canContinueNPC, 100, 30000);
    }
}
