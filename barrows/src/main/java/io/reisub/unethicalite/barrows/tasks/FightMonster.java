package io.reisub.unethicalite.barrows.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.NPCs;
import dev.hoot.api.entities.Players;
import dev.hoot.api.game.GameThread;
import dev.hoot.api.movement.Reachable;
import io.reisub.unethicalite.barrows.Barrows;
import io.reisub.unethicalite.barrows.Brother;
import io.reisub.unethicalite.barrows.Config;
import io.reisub.unethicalite.barrows.Potential;
import io.reisub.unethicalite.barrows.Room;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.NPC;

import javax.inject.Inject;

public class FightMonster extends Task {
    @Inject
    private Barrows plugin;

    @Inject
    private Config config;

    private NPC target;

    @Override
    public String getStatus() {
        return "Fighting monster";
    }

    @Override
    public boolean validate() {
        if (config.potential() == Potential.WHATEVER) {
            return false;
        }

        if (Players.getLocal().getInteracting() != null) {
            return false;
        }

        if (!Utils.isInRegion(Barrows.CRYPT_REGION) && Players.getLocal().getWorldLocation().getPlane() != 0) {
            return false;
        }

        int potential = getPotentialWithLastBrother();

        if (Room.getCurrentRoom() == Room.C && potential < config.potential().getMinimum()) {
            target = NPCs.getNearest(n -> n.hasAction("Attack") && Reachable.isInteractable(n));

            return target != null;
        }

        target = NPCs.getNearest(n -> n.getInteracting() != null && n.getInteracting().equals(Players.getLocal()));

        if (target == null || !Reachable.isInteractable(target)) {
            return false;
        }

        if (potential + target.getCombatLevel() > config.potential().getMaximum()) {
            return false;
        }

        return potential < config.potential().getMinimum() || config.potential().isTryMaximum();
    }

    @Override
    public void execute() {
        GameThread.invoke(() -> target.interact("Attack"));
        Time.sleepTicksUntil(() -> Players.getLocal().getInteracting() != null, 3);
    }

    private int getPotentialWithLastBrother() {
        int potentional = plugin.getRewardPotential();

        for (Brother brother : Brother.values()) {
            if (!brother.isDead()) {
                switch (plugin.getCurrentBrother()) {
                    case KARIL:
                    case AHRIM:
                        return plugin.getRewardPotential() + 100;
                    default:
                        return plugin.getRewardPotential() + 117;
                }
            }
        }

        return potentional;
    }
}
