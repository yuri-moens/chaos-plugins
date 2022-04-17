package io.reisub.unethicalite.barrows.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.game.Skills;
import io.reisub.unethicalite.barrows.Barrows;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ObjectID;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;

public class DrinkPool extends Task {
    private TileObject pool;

    @Override
    public String getStatus() {
        return "Drinking from pool";
    }

    @Override
    public boolean validate() {
        return Utils.isInRegion(Barrows.FEROX_ENCLAVE_REGIONS)
                && Skills.getBoostedLevel(Skill.PRAYER) < Skills.getLevel(Skill.PRAYER)
                && (pool = TileObjects.getNearest(ObjectID.POOL_OF_REFRESHMENT, ObjectID.ORNATE_POOL_OF_REJUVENATION, ObjectID.FROZEN_ORNATE_POOL_OF_REJUVENATION)) != null;

    }

    @Override
    public void execute() {
        GameThread.invoke(() -> pool.interact("Drink"));
        Time.sleepTicksUntil(() -> Skills.getBoostedLevel(Skill.PRAYER) == Skills.getLevel(Skill.PRAYER), 20);
        Time.sleepTicks(3);
    }
}
