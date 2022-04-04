package io.reisub.unethicalite.chompychomper.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.NPCs;
import dev.hoot.api.game.GameThread;
import io.reisub.unethicalite.chompychomper.ChompyChomper;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import java.util.LinkedList;

public class Shoot extends Task {
    @Inject
    private ChompyChomper plugin;

    private final LinkedList<NPC> chompies = new LinkedList<>();

    @Override
    public String getStatus() {
        return "Shooting chompy";
    }

    @Override
    public boolean validate() {
        return plugin.getCurrentActivity() == Activity.IDLE
                && !chompies.isEmpty();
    }

    @Override
    public void execute() {
        NPC chompy = chompies.poll();
        if (chompy == null || chompy.isDead()) {
            return;
        }

        GameThread.invoke(() -> chompy.interact("Attack"));
        if (!Time.sleepTicksUntil(() -> plugin.getCurrentActivity() == Activity.ATTACKING, 3)) {
            chompies.addFirst(chompy);
        }
    }

    @Subscribe
    private void onNpcSpawned(NpcSpawned event) {
        if (!plugin.isRunning()) {
            return;
        }

        if (event.getNpc().getId() == NpcID.CHOMPY_BIRD) {
            chompies.add(event.getNpc());
        }
    }

    @Subscribe
    private void onNpcDespawned(NpcDespawned event) {
        if (!plugin.isRunning()) {
            return;
        }

        if (event.getNpc().getId() == NpcID.CHOMPY_BIRD) {
            chompies.remove(event.getNpc());
        }
    }
}