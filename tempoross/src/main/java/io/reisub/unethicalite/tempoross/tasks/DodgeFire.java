package io.reisub.unethicalite.tempoross.tasks;

import dev.hoot.api.coords.RectangularArea;
import dev.hoot.api.entities.NPCs;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.movement.Movement;
import io.reisub.unethicalite.tempoross.Tempoross;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;

import javax.inject.Inject;

public class DodgeFire extends Task {
    @Inject
    private Tempoross plugin;

    @Override
    public String getStatus() {
        return "Dodging fire";
    }

    @Override
    public boolean validate() {
        if (!plugin.isInTemporossArea()) return false;

        NPC fire = NPCs.getNearest((n) -> n.getId() == NpcID.FIRE_8643
                && (plugin.getIslandArea().contains(n) || plugin.getBoatArea().contains(n)));

         if (fire == null) {
            return false;
        }

         int x = fire.getWorldLocation().getX();
         int y = fire.getWorldLocation().getY();

        RectangularArea fireArea = new RectangularArea(x, y, x + 1, y + 1);

        return fireArea.contains(Players.getLocal());
    }

    @Override
    public void execute() {
        if (plugin.getCurrentActivity() == Activity.STOCKING_CANNON) {
            NPC northAmmoCrate = NPCs.getNearest(NpcID.AMMUNITION_CRATE);
            Movement.walk(northAmmoCrate.getWorldLocation().dy(-1));
        } else if (plugin.getCurrentActivity() == Activity.FISHING) {
            TileObject shrine = TileObjects.getNearest(ObjectID.SHRINE_41236);
            if (shrine == null) return;

            shrine.interact(0);
        }
    }
}
