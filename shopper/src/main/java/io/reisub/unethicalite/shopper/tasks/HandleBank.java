package io.reisub.unethicalite.shopper.tasks;

import dev.hoot.api.commons.Rand;
import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.game.Game;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.movement.Movement;
import io.reisub.unethicalite.shopper.Shopper;
import io.reisub.unethicalite.utils.tasks.BankTask;
import net.runelite.api.coords.WorldPoint;

import javax.inject.Inject;
import java.time.Duration;

public class HandleBank extends BankTask {
    @Inject
    private Shopper plugin;

    @Override
    public boolean validate() {
        return Inventory.isFull()
                && isLastBankDurationAgo(Duration.ofSeconds(3));
    }

    @Override
    public void execute() {
        if (plugin.getBankLocation() != null) {
            WorldPoint target = plugin.getBankLocation().dx(Rand.nextInt(-1, 2)).dy(Rand.nextInt(-1, 2));
            int start = Game.getClient().getTickCount();

            while (Players.getLocal().distanceTo(target) > 10 && Game.getClient().getTickCount() <= start + 100) {
                if (!Movement.isWalking()) {
                    Movement.walkTo(target);

                    if (!Players.getLocal().isMoving()) {
                        Time.sleepTick();
                    }
                }

                Time.sleepTick();
            }
        }

        open();


    }
}
