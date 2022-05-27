package io.reisub.unethicalite.mta.tasks;

import io.reisub.unethicalite.mta.Config;
import io.reisub.unethicalite.utils.tasks.Task;
import java.awt.event.KeyEvent;
import javax.inject.Inject;
import net.runelite.api.NPC;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.input.KeyListener;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.magic.Magic;
import net.unethicalite.api.magic.SpellBook;
import net.unethicalite.api.widgets.Widgets;

public class TelekineticGrab extends Task implements KeyListener {
  @Inject private Config config;

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

    final NPC mazeGuardian = NPCs.getNearest(n -> n.getName().contains("Maze Guardian"));
    if (mazeGuardian == null) {
      return;
    }

    GameThread.invoke(() -> Magic.cast(SpellBook.Standard.TELEKINETIC_GRAB, mazeGuardian));
  }

  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void keyPressed(KeyEvent e) {
    if (config.enableTelegrabHotkey()
        && config.spellHotkey().matches(e)
        && Widgets.isVisible(Widgets.get(WidgetID.MTA_TELEKINETIC_GROUP_ID, 0))) {
      cast = true;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {}
}
