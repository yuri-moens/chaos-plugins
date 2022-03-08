package io.reisub.unethicalite.cooking.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.game.Game;
import dev.hoot.api.items.Inventory;
import io.reisub.unethicalite.cooking.Config;
import io.reisub.unethicalite.cooking.Cooking;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Drop extends Task {
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
        // TODO: move to oven in Hosidius before dropping

        Inventory.getAll(config.foodId()).forEach((i) -> i.interact("Drop"));

        Time.sleepTick();

        plugin.setLastDrop(Game.getClient().getTickCount());
    }
}
