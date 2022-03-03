package io.reisub.unethicalite.tempoross.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.NPCs;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.items.Inventory;
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
        NPC pudi = NPCs.getNearest(NpcID.CAPTAIN_PUDI_10585, NpcID.CAPTAIN_PUDI_10586);

        if (pudi != null && Players.getLocal().distanceTo(pudi) < 5) {
            return true;
        }

        return plugin.isFinished();
    }

    @Override
    public void execute() {
        if (!plugin.isFinished()) {
            NPC pudi = NPCs.getNearest(NpcID.CAPTAIN_PUDI_10585, NpcID.CAPTAIN_PUDI_10586);
            pudi.interact("Forfeit");
            Time.sleepUntil(() -> plugin.isInDesert(), 20000);
        }

        TileObject buckets = TileObjects.getNearest(ObjectID.BUCKETS_40966);
        if (buckets == null) return;

        buckets.interact("Take-5");
        Time.sleepUntil(() -> Inventory.contains(ItemID.BUCKET), 100, 10000);

        NPC deri = NPCs.getNearest(NpcID.FIRST_MATE_DERI_10595);
        if (deri == null) return;

        deri.interact("Leave");
        Time.sleepUntil(() -> plugin.isInDesert(), 20000);

        Time.sleepUntil(Dialog::canContinueNPC, 100, 30000);
    }
}
