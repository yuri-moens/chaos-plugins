package io.reisub.unethicalite.barrows.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.game.Skills;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.magic.Magic;
import dev.hoot.api.magic.Regular;
import io.reisub.unethicalite.barrows.Barrows;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;

import javax.inject.Inject;

public class DrinkPool extends Task {
    @Inject
    private Barrows plugin;

    @Override
    public String getStatus() {
        return "Drinking from pool";
    }

    @Override
    public boolean validate() {
        return (plugin.isInFeroxEnclave()) // TODO check for house
                && Skills.getBoostedLevel(Skill.PRAYER) < Skills.getLevel(Skill.PRAYER);

    }

    @Override
    public void execute() {
        TileObject pool = TileObjects.getNearest(ObjectID.POOL_OF_REFRESHMENT, ObjectID.ORNATE_POOL_OF_REJUVENATION, ObjectID.FROZEN_ORNATE_POOL_OF_REJUVENATION);
        if (pool == null) {
            return;
        }

        pool.interact("Drink");
        Time.sleepTicksUntil(() -> Skills.getBoostedLevel(Skill.PRAYER) == Skills.getLevel(Skill.PRAYER), 20);

        Time.sleepTicks(3);

        Item barrowsTeleport = Inventory.getFirst(ItemID.BARROWS_TELEPORT);
        Item houseTeleport = Inventory.getFirst(ItemID.TELEPORT_TO_HOUSE);

        if (barrowsTeleport != null) {
            barrowsTeleport.interact(0);
        } else if (houseTeleport != null) {
            houseTeleport.interact(0);
        } else {
            Magic.cast(Regular.TELEPORT_TO_HOUSE);
        }

        Time.sleepTicks(3);
    }
}
