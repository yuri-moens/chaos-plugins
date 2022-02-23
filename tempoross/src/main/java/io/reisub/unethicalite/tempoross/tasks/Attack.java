package io.reisub.unethicalite.tempoross.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.NPCs;
import io.reisub.unethicalite.tempoross.Tempoross;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;

import javax.inject.Inject;

public class Attack extends Task {
    @Inject
    private Tempoross plugin;

    private NPC pool;

    @Override
    public String getStatus() {
        return "Attacking";
    }

    @Override
    public boolean validate() {
        if (!plugin.isInTemporossArea() || plugin.getCurrentActivity() != Activity.IDLE) return false;

        // don't attack when he's almost dead but we still have a phase to do
        if (plugin.getPhase() == 3 && plugin.getEssence() <= 10) return false;

        // don't attack in phase 4 when we still have cooked fish
        if (plugin.getPhase() >= 4 && plugin.getCookedFish() > 0) return false;

        pool = NPCs.getNearest((n) -> n.getId() == NpcID.SPIRIT_POOL && plugin.getIslandArea().contains(n));

        return pool != null;
    }

    @Override
    public void execute() {
        pool.interact(0);
        Time.sleepUntil(() -> plugin.getCurrentActivity() == Activity.FISHING, 50, 5000);
    }
}
