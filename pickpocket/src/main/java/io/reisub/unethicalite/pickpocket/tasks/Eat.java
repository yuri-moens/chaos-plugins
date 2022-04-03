package io.reisub.unethicalite.pickpocket.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.game.Skills;
import dev.hoot.api.items.Inventory;
import dev.hoot.bot.managers.Static;
import io.reisub.unethicalite.pickpocket.Config;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Skill;

import javax.inject.Inject;

public class Eat extends Task {
    @Inject
    private Config config;

    private int last;

    @Override
    public String getStatus() {
        return "Eating";
    }

    @Override
    public boolean validate() {
        return Skills.getBoostedLevel(Skill.HITPOINTS) <= config.eatHP()
                && Inventory.contains(config.food())
                && Static.getClient().getTickCount() > last + 3;
    }

    @Override
    public void execute() {
        Inventory.getFirst(config.food()).interact(0);
        Time.sleepTick();

        last = Static.getClient().getTickCount();
    }
}
