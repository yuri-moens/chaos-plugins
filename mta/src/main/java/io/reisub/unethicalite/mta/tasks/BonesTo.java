package io.reisub.unethicalite.mta.tasks;

import io.reisub.unethicalite.mta.Config;
import io.reisub.unethicalite.utils.tasks.Task;
import java.awt.event.KeyEvent;
import javax.inject.Inject;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.input.KeyListener;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.magic.Magic;
import net.unethicalite.api.magic.SpellBook;
import net.unethicalite.api.widgets.Widgets;

public class BonesTo extends Task implements KeyListener {
  @Inject private Config config;

  private boolean cast;

  @Override
  public String getStatus() {
    return "Casting bones to";
  }

  @Override
  public boolean validate() {
    return cast;
  }

  @Override
  public int execute() {
    cast = false;

    final TileObject foodChute = TileObjects.getNearest(ObjectID.FOOD_CHUTE);
    if (foodChute == null) {
      return 1;
    }

    GameThread.invoke(() -> foodChute.interact("Deposit"));
    Time.sleepTick();

    GameThread.invoke(() -> Magic.cast(SpellBook.Standard.BONES_TO_BANANAS));
    return 1;
  }

  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void keyPressed(KeyEvent e) {
    if (config.enableBonesHotkey()
        && config.spellHotkey().matches(e)
        && Widgets.isVisible(Widgets.get(WidgetID.MTA_GRAVEYARD_GROUP_ID, 0))) {
      cast = true;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {}
}
