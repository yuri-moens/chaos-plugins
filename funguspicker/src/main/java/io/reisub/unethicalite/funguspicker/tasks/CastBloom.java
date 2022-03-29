package io.reisub.unethicalite.funguspicker.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.game.Skills;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.movement.Movement;
import io.reisub.unethicalite.funguspicker.FungusPicker;
import io.reisub.unethicalite.utils.api.Interact;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ObjectID;
import net.runelite.api.Skill;

import javax.inject.Inject;

public class CastBloom extends Task {
    @Inject
    private FungusPicker plugin;

    @Override
    public String getStatus() {
        return "Casting bloom";
    }

    @Override
    public boolean validate() {
        return !Inventory.isFull()
                && TileObjects.getNearest(ObjectID.FUNGI_ON_LOG) == null
                && Skills.getBoostedLevel(Skill.PRAYER) > 0;
    }

    @Override
    public void execute() {
        if (!Players.getLocal().getWorldLocation().equals(FungusPicker.FUNGUS_LOCATION)) {
            Movement.walk(FungusPicker.FUNGUS_LOCATION);

            Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().equals(FungusPicker.FUNGUS_LOCATION), 10);
        }

        boolean interacted = Interact.interactWithInventoryOrEquipment((i) -> i.hasAction("Bloom"), "Bloom", null, 0);
        if (!interacted) {
            plugin.stop("Couldn't find an item to cast Bloom with. Stopping plugin.");
            return;
        }

        Time.sleepTicks(3);
    }
}