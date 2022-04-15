package io.reisub.unethicalite.tempoross.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.NPCs;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.widgets.Dialog;
import io.reisub.unethicalite.tempoross.Tempoross;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;

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
