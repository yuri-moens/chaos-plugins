package io.reisub.unethicalite.mining.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.movement.Movement;
import io.reisub.unethicalite.mining.Config;
import io.reisub.unethicalite.utils.api.CMovement;
import io.reisub.unethicalite.utils.tasks.Task;

import javax.inject.Inject;

public class GoToMiningArea extends Task {
    @Inject
    private Config config;

    @Override
    public String getStatus() {
        return "Going to mining area";
    }

    @Override
    public boolean validate() {
        return Players.getLocal().distanceTo(config.location().getMiningAreaPoint()) > 8
                && !Inventory.isFull();
    }

    @Override
    public void execute() {
        if (!Movement.isRunEnabled()) {
            if (!config.location().isThreeTick() || (config.location().isThreeTick() && Movement.getRunEnergy() > 70)) {
                Movement.toggleRun();
            }
        }

        if (config.location().isThreeTick()) {
            CMovement.walkTo(config.location().getMiningAreaPoint());

            Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().equals(config.location().getMiningAreaPoint()), 20);
        } else {
            CMovement.walkTo(config.location().getMiningAreaPoint(), 2);
        }
    }
}
