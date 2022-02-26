package io.reisub.unethicalite.tempoross.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.NPCs;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.packets.NPCPackets;
import io.reisub.unethicalite.tempoross.Tempoross;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.NullObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldArea;

import javax.inject.Inject;

@Slf4j
public class DodgeFire extends Task {
    @Inject
    private Tempoross plugin;

    @Override
    public String getStatus() {
        return "Dodging fire";
    }

    @Override
    public boolean validate() {
        if (!plugin.isInTemporossArea() || Players.getLocal().isMoving()) return false;

        TileObject fire = TileObjects.getNearest((o) -> o.getId() == NullObjectID.NULL_41006
                && (plugin.getIslandArea().contains(o) || plugin.getBoatArea().contains(o)));

         if (fire == null) {
            return false;
        }

         WorldArea fireArea = new WorldArea(fire.getWorldLocation(), 2, 2);

        return fireArea.contains(Players.getLocal());
    }

    @Override
    public void execute() {
        if (plugin.getCurrentActivity() == Activity.STOCKING_CANNON) {
            NPC southAmmoCrate = NPCs.getNearest(NpcID.AMMUNITION_CRATE_10577);
            NPCPackets.npcFirstOption(southAmmoCrate, false);
            Time.sleepTicksUntil(() -> !Players.getLocal().isMoving(), 3);
        } else if (plugin.getCurrentActivity() == Activity.FISHING) {
            plugin.setActivity(Activity.IDLE);
        }
    }
}
