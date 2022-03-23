package io.reisub.unethicalite.farming.tasks.herb;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.magic.Magic;
import dev.hoot.api.magic.Regular;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.CMovement;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;

public class Falador extends Task {
    private boolean done;

    private final WorldPoint patchPoint = new WorldPoint(0,0, 0);

    @Override
    public String getStatus() {
        return "Going to Falador herb patch";
    }

    @Override
    public boolean validate() {
        return !done;
    }

    @Override
    public void execute() {
        Item explorersRing = Inventory.getFirst(Predicates.ids(Constants.EXPLORERS_RING_IDS));

        if (explorersRing != null) {
            explorersRing.interact("Teleport");
        } else {
            Magic.cast(Regular.FALADOR_TELEPORT);
        }

        CMovement.walkTo(patchPoint);

        if (Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().equals(patchPoint), 20)) {
            done = true;
        }
    }
}
