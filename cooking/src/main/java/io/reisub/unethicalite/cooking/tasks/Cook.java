package io.reisub.unethicalite.cooking.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.game.Game;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.widgets.Production;
import io.reisub.unethicalite.cooking.Config;
import io.reisub.unethicalite.cooking.Cooking;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Item;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;

@RequiredArgsConstructor
public class Cook extends Task {
    private final Cooking plugin;
    private final Config config;

    private int last;

    @Override
    public String getStatus() {
        return "Cooking";
    }

    @Override
    public boolean validate() {
        int count = Inventory.getCount(config.foodId());
        return (plugin.getCurrentActivity() == Activity.IDLE || count == 1)
                && (count > 0 || plugin.getLastBank() + 1 >= Game.getClient().getTickCount())
                && Game.getClient().getTickCount() >= last + 3;
    }

    @Override
    public void execute() {
        TileObject oven = TileObjects.getNearest(ObjectID.CLAY_OVEN_21302);
        TileObject fire = TileObjects.getNearest("Fire");
        if (oven == null && fire == null) {
            return;
        }

        int count = Inventory.getCount(config.foodId());

        if (oven != null) {
            oven.interact(0);
        } else {
            Item rawFood = Inventory.getFirst(config.foodId());
            if (rawFood == null) {
                return;
            }

            rawFood.useOn(fire);
        }

        if (count > 1 || (count == 0 && plugin.getLastBank() + 1 >= Game.getClient().getTickCount())) {
            Time.sleepTicksUntil(Production::isOpen, 20);
        }

        if (Production.isOpen()) {
            Production.chooseOption(1);

            Time.sleepTicksUntil(() -> plugin.getCurrentActivity() == Activity.COOKING, 5);
        }

        last = Game.getClient().getTickCount();
    }
}
