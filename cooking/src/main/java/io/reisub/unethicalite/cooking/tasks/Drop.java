package io.reisub.unethicalite.cooking.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.game.Game;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.movement.Movement;
import io.reisub.unethicalite.cooking.Config;
import io.reisub.unethicalite.cooking.Cooking;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.RequiredArgsConstructor;
import net.runelite.api.coords.WorldPoint;

@RequiredArgsConstructor
public class Drop extends Task {
    private static final WorldPoint HOSIDIUS_COOKING_SPOT = new WorldPoint(1677, 3621, 0);

    private final Cooking plugin;
    private final Config config;

    @Override
    public String getStatus() {
        return "Dropping food";
    }

    @Override
    public boolean validate() {
        int count = Inventory.getCount(config.foodId());
        return config.sonicMode()
                && (plugin.getCurrentActivity() == Activity.IDLE || count == 1)
                && (count > 0 || plugin.getLastBank() + 1 >= Game.getClient().getTickCount())
                && Game.getClient().getTickCount() >= plugin.getLastDrop() + 3;

    }

    @Override
    public void execute() {
        if (Players.getLocal().distanceTo(HOSIDIUS_COOKING_SPOT) < 10
                && !Players.getLocal().getWorldLocation().equals(HOSIDIUS_COOKING_SPOT)) {
            Movement.walk(HOSIDIUS_COOKING_SPOT);

            if (!Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().equals(HOSIDIUS_COOKING_SPOT), 10)) {
                return;
            }
        }

        Inventory.getAll(config.foodId()).forEach((i) -> i.interact("Drop"));

        Time.sleepTick();

        plugin.setLastDrop(Game.getClient().getTickCount());
    }
}
