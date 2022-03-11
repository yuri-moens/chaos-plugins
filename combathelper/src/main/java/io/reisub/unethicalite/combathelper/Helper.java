package io.reisub.unethicalite.combathelper;

import net.runelite.client.input.KeyListener;

import javax.inject.Inject;
import java.awt.event.KeyEvent;

public abstract class Helper implements KeyListener {
    @Inject
    protected CombatHelper plugin;

    @Inject
    protected Config config;

    public void startUp() {

    }

    public void shutDown() {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
