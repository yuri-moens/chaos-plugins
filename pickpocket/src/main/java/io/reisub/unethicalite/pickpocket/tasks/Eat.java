package io.reisub.unethicalite.pickpocket.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.game.Skills;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.managers.Static;
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
        return (Skills.getBoostedLevel(Skill.HITPOINTS) <= config.eatHP() || config.healAtBank())
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
