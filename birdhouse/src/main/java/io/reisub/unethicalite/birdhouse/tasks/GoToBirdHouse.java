package io.reisub.unethicalite.birdhouse.tasks;

import dev.hoot.api.commons.Rand;
import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.movement.Movement;
import io.reisub.unethicalite.birdhouse.BirdHouse;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.AllArgsConstructor;
import net.runelite.api.coords.WorldPoint;

@AllArgsConstructor
public class GoToBirdHouse extends Task {
    private final BirdHouse plugin;
    private final WorldPoint target = new WorldPoint(3681, 3819, 0);

    @Override
    public String getStatus() {
        return "Going to southern bird house";
    }

    @Override
    public boolean validate() {
        return plugin.isEmptied(Constants.MEADOW_NORTH_SPACE)
                && !plugin.isEmptied(Constants.MEADOW_SOUTH_SPACE)
                && Players.getLocal().distanceTo(target) > 15;
    }

    @Override
    public void execute() {
        WorldPoint randomTarget = target.dx(Rand.nextInt(-2, 3)).dy(Rand.nextInt(-2, 3));

        while (Players.getLocal().distanceTo(randomTarget) > 10) {
            if (!Movement.isWalking()) {
                Movement.walkTo(randomTarget);
            }

            Time.sleepTick();
        }
    }
}
