package io.reisub.unethicalite.mta.tasks;

import dev.hoot.api.entities.NPCs;
import dev.hoot.api.game.GameThread;
import dev.hoot.api.magic.Magic;
import dev.hoot.api.magic.Regular;
import dev.hoot.api.widgets.Widgets;
import io.reisub.unethicalite.mta.Config;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.NPC;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.input.KeyListener;

import javax.inject.Inject;
import java.awt.event.KeyEvent;

public class TelekineticGrab extends Task implements KeyListener {
    @Inject
    private Config config;

    private boolean cast;

    @Override
    public String getStatus() {
        return "Casting telekinetic grab";
    }

    @Override
    public boolean validate() {
        return cast;
    }

    @Override
    public void execute() {
        cast = false;

        NPC mazeGuardian = NPCs.getNearest(n -> n.getName().contains("Maze Guardian"));
        if (mazeGuardian == null) {
            return;
        }

        GameThread.invoke(() -> Magic.cast(Regular.TELEKINETIC_GRAB, mazeGuardian));
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (config.enableTelegrabHotkey()
                && config.spellHotkey().matches(e)
                && Widgets.isVisible(Widgets.get(WidgetID.MTA_TELEKINETIC_GROUP_ID, 0))) {
            cast = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
