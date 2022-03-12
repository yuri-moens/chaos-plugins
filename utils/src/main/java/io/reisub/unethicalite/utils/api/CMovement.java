package io.reisub.unethicalite.utils.api;

import dev.hoot.api.commons.Rand;
import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.game.Game;
import dev.hoot.api.movement.Movement;
import dev.hoot.bot.managers.Static;
import net.runelite.api.GameState;
import net.runelite.api.coords.WorldPoint;

public class CMovement {
    private static final int DEFAULT_TIMEOUT = 100;
    private static final int DESTINATION_DISTANCE = 8;

    public static void walkTo(WorldPoint destination) {
        walkTo(destination, 0);
    }

    public static void walkTo(WorldPoint destination, Runnable task) {
        walkTo(destination, 0, task);
    }

    public static void walkTo(WorldPoint destination, int radius) {
        walkTo(destination, radius, null, DEFAULT_TIMEOUT);
    }

    public static void walkTo(WorldPoint destination, int radius, Runnable task) {
        walkTo(destination, radius, task, DEFAULT_TIMEOUT);
    }

    public static void walkTo(WorldPoint destination, int radius, Runnable task, int tickTimeout) {
        int start = Game.getClient().getTickCount();

        if (radius > 0) {
            destination = destination.dx(Rand.nextInt(-radius, radius + 1)).dy(Rand.nextInt(-radius, radius + 1));
        }

        while (Players.getLocal().distanceTo(destination) > DESTINATION_DISTANCE && Game.getClient().getTickCount() <= start + tickTimeout) {
            if (!Movement.isWalking() && Static.getClient().getGameState() != GameState.LOADING) {
                Movement.walkTo(destination);

                if (!Players.getLocal().isMoving()) {
                    Time.sleepTick();
                }
            } else if (task != null) {
                Static.getClientThread().invoke(task);
            }

            Time.sleepTick();
        }
    }
}
