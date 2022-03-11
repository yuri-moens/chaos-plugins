package io.reisub.unethicalite.daeyaltessence.tasks;

import dev.hoot.api.commons.Rand;
import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.game.Game;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.movement.Movement;
import io.reisub.unethicalite.daeyaltessence.DaeyaltEssence;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;

public class GoToMine extends Task {
    private static final WorldPoint ESSENCE_MINE_LADDER = new WorldPoint(3635, 3340, 0);

    @Override
    public String getStatus() {
        return "Going to essence mine";
    }

    @Override
    public boolean validate() {
        return Players.getLocal().getWorldLocation().getRegionID() != DaeyaltEssence.ESSENCE_MINE_REGION;
    }

    @Override
    public void execute() {
        WorldPoint target = ESSENCE_MINE_LADDER.dx(Rand.nextInt(-1, 2)).dy(Rand.nextInt(-1, 2));
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

        TileObject ladder = TileObjects.getNearest(ObjectID.STAIRCASE_39092);
        if (ladder == null) {
            return;
        }

        ladder.interact(0);
        Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().getRegionID() == DaeyaltEssence.ESSENCE_MINE_REGION, 20);

        Time.sleepTicks(2);

        Inventory.getAll((i) -> i.getName().contains("Graceful")).forEach((i) -> i.interact("Wear"));
    }
}
