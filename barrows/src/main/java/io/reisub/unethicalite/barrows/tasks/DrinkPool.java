package io.reisub.unethicalite.barrows.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.game.Skills;
import io.reisub.unethicalite.barrows.Barrows;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ObjectID;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;

public class DrinkPool extends Task {
    @Override
    public String getStatus() {
        return "Drinking from pool";
    }

    @Override
    public boolean validate() {
        return Utils.isInRegion(Barrows.FEROX_ENCLAVE_REGIONS)
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
    }
}
