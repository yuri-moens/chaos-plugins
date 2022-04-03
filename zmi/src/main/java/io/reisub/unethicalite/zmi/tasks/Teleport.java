package io.reisub.unethicalite.zmi.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.magic.Lunar;
import dev.hoot.api.magic.Magic;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import io.reisub.unethicalite.zmi.Zmi;

public class Teleport extends Task {
    @Override
    public String getStatus() {
        return "Teleporting";
    }

    @Override
    public boolean validate() {
        return !Inventory.contains(Predicates.ids(Constants.ESSENCE_IDS))
                && Players.getLocal().distanceTo(Zmi.NEAR_ALTAR) < 5
                && Zmi.pouchesAreEmpty;
    }

    @Override
    public void execute() {
        Magic.cast(Lunar.OURANIA_TELEPORT);
        Time.sleepTicksUntil(() -> Players.getLocal().distanceTo(Zmi.NEAR_ALTAR) > 5, 10);
        Time.sleepTick();
    }
}
