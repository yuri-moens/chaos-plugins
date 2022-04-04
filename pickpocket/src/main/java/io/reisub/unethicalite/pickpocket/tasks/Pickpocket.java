package io.reisub.unethicalite.pickpocket.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.NPCs;
import dev.hoot.api.entities.Players;
import dev.hoot.api.game.GameThread;
import dev.hoot.api.game.Skills;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.movement.Reachable;
import io.reisub.unethicalite.pickpocket.Config;
import io.reisub.unethicalite.utils.api.CMovement;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.NPC;
import net.runelite.api.Skill;

import javax.inject.Inject;

public class Pickpocket extends Task {
    @Inject
    private io.reisub.unethicalite.pickpocket.Pickpocket plugin;

    @Inject
    private Config config;

    @Override
    public String getStatus() {
        return "Pickpocketing";
    }

    @Override
    public boolean validate() {
        return plugin.getCurrentActivity() == Activity.IDLE
                && !Inventory.isFull()
                && Players.getLocal().getModelHeight() != 1000
                && (Inventory.contains(config.food()) || Skills.getBoostedLevel(Skill.HITPOINTS) > config.eatHP());
    }

    @Override
    public void execute() {
        NPC target = NPCs.getNearest(Predicates.ids(config.target().getIds()));
        if (target == null) {
            CMovement.walkTo(config.target().getNearestPickpocketLocation(), 2);

            return;
        }

        if (!Reachable.isInteractable(target)) {
            CMovement.walkTo(target.getWorldLocation());

            if (!Time.sleepTicksUntil(() -> Reachable.isInteractable(target), 20)) {
                return;
            }
        }

        GameThread.invoke(() -> target.interact("Pickpocket"));
        plugin.setActivity(Activity.THIEVING);
    }
}
