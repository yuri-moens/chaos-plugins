package io.reisub.unethicalite.combathelper;

import java.awt.event.KeyEvent;
import javax.inject.Inject;
import net.runelite.client.input.KeyListener;

public abstract class Helper implements KeyListener {
  @Inject protected CombatHelper plugin;

  @Inject protected Config config;

  public void startUp() {}

  public void shutDown() {}

  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void keyPressed(KeyEvent e) {}

  @Override
  public void keyReleased(KeyEvent e) {}
}
