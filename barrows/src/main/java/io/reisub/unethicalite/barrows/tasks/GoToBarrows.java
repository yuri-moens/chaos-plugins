package io.reisub.unethicalite.barrows.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.game.Skills;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.magic.Magic;
import dev.hoot.api.magic.Necromancy;
import dev.hoot.api.magic.Regular;
import dev.hoot.api.movement.Movement;
import dev.hoot.bot.managers.Static;
import io.reisub.unethicalite.barrows.Barrows;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;

public class GoToBarrows extends Task {
    @Override
    public String getStatus() {
        return "Going to barrows";
    }

    @Override
    public boolean validate() {
        return Skills.getBoostedLevel(Skill.PRAYER) >= Skills.getLevel(Skill.PRAYER)
                && (Utils.isInRegion(Barrows.FEROX_ENCLAVE_REGIONS) || Static.getClient().isInInstancedRegion());
    }

    @Override
    public void execute() {
        if (!Movement.isRunEnabled()) {
            Movement.toggleRun();
        }

        if (!Static.getClient().isInInstancedRegion()) {
            WorldPoint current = Players.getLocal().getWorldLocation();

            Item barrowsTeleport = Inventory.getFirst(ItemID.BARROWS_TELEPORT);
            Item houseTeleport = Inventory.getFirst(ItemID.TELEPORT_TO_HOUSE);

            if (barrowsTeleport != null) {
                barrowsTeleport.interact(0);
            } else if (Necromancy.BARROWS_TELEPORT.canCast()) {
                Magic.cast(Necromancy.BARROWS_TELEPORT);
            } else if (houseTeleport != null) {
                houseTeleport.interact(0);
            } else {
                Magic.cast(Regular.TELEPORT_TO_HOUSE);
            }

            Time.sleepTicksUntil(() -> !Players.getLocal().getWorldLocation().equals(current), 10);
            Time.sleepTicks(2);
        }

        if (Static.getClient().isInInstancedRegion()) {
            TileObject portal = TileObjects.getNearest(Predicates.ids(Constants.PORTAL_NEXUS_IDS));
            if (portal == null) {
                return;
            }

            portal.interact("Barrows");
            Time.sleepTicksUntil(() -> Utils.isInRegion(Barrows.BARROWS_REGION), 20);
        }
    }
}
