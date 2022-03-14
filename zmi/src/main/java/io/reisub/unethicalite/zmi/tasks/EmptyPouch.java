package io.reisub.unethicalite.zmi.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.items.Inventory;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import io.reisub.unethicalite.zmi.Zmi;

public class EmptyPouch extends Task {
    @Override
    public String getStatus() {
        return "Emptying pouches";
    }

    @Override
    public boolean validate() {
        return !Zmi.pouchesAreEmpty
                && !Inventory.contains(Predicates.ids(Constants.ESSENCE_IDS))
                && Players.getLocal().distanceTo(Zmi.NEAR_ALTAR) < 5
                && Players.getLocal().isIdle();
    }

    @Override
    public void execute() {
        Inventory.getAll(Predicates.ids(Constants.ESSENCE_POUCH_IDS)).forEach((i) -> i.interact("Empty"));
        if (!Time.sleepTicksUntil(() -> Inventory.contains(Predicates.ids(Constants.ESSENCE_IDS)), 3)) {
            Zmi.pouchesAreEmpty = true;
        }
    }
}
