package io.reisub.unethicalite.pickpocket.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.game.Combat;
import dev.hoot.api.game.Game;
import dev.hoot.api.game.Skills;
import dev.hoot.api.items.Bank;
import dev.hoot.api.items.Inventory;
import io.reisub.unethicalite.pickpocket.Config;
import io.reisub.unethicalite.pickpocket.Pickpocket;
import io.reisub.unethicalite.utils.api.CMovement;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.BankTask;
import net.runelite.api.Item;
import net.runelite.api.Skill;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.ItemStatChanges;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.itemstats.StatsChanges;
import net.runelite.client.plugins.itemstats.stats.Stats;

import javax.inject.Inject;

public class HandleBank extends BankTask {
    @Inject
    private ItemStatChanges statChanges;

    @Inject
    private Pickpocket plugin;

    @Inject
    private Config config;

    @Override
    public boolean validate() {
        return plugin.getCurrentActivity() == Activity.IDLE
                && Players.getLocal().getModelHeight() != 1000
                && (
                        Inventory.isFull()
                        || (!Inventory.contains(config.food()) && Skills.getBoostedLevel(Skill.HITPOINTS) <= config.eatHP())
                );
    }

    @Override
    public void execute() {
        if (!open()) {
            CMovement.walkTo(plugin.getNearestLocation().getBankLocation(), 1);

            return;
        }

        Bank.depositInventory();

        Item food = Bank.getFirst(config.food());
        if (food == null) {
            return;
        }

        int quantity = config.healAtBank() ? Math.floorDiv(Combat.getMissingHealth(), heals(food.getId())) : config.foodQuantity();

        Bank.withdraw(config.food(), quantity, Bank.WithdrawMode.ITEM);

        close();

        if (config.healAtBank() && quantity > 0) {
            Time.sleepTicksUntil(() -> Inventory.contains(config.food()), 5);

            Inventory.getAll(config.food()).forEach((i) -> {
                food.interact(0);
                Time.sleepTicks(3);
            });
        }
    }

    private int heals(int itemId) {
        Effect effect = statChanges.get(itemId);
        if (effect != null) {
            StatsChanges statsChanges = effect.calculate(Game.getClient());
            for (StatChange statChange : statsChanges.getStatChanges()) {
                if (statChange.getStat().getName().equals(Stats.HITPOINTS.getName())) {
                    return statChange.getTheoretical();
                }
            }
        }

        return 0;
    }
}
