package io.reisub.unethicalite.mta.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.magic.Magic;
import dev.unethicalite.api.magic.Regular;
import dev.unethicalite.api.widgets.Widgets;
import io.reisub.unethicalite.mta.Config;
import io.reisub.unethicalite.utils.tasks.Task;
import java.awt.event.KeyEvent;
import javax.inject.Inject;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.input.KeyListener;

public class BonesTo extends Task implements KeyListener {
  @Inject
  private Config config;

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
  public void execute() {
    cast = false;

    TileObject foodChute = TileObjects.getNearest(ObjectID.FOOD_CHUTE);
    if (foodChute == null) {
      return;
    }

    GameThread.invoke(() -> foodChute.interact("Deposit"));
    Time.sleepTick();

    GameThread.invoke(() -> Magic.cast(Regular.BONES_TO_BANANAS));
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (config.enableBonesHotkey()
        && config.spellHotkey().matches(e)
        && Widgets.isVisible(Widgets.get(WidgetID.MTA_GRAVEYARD_GROUP_ID, 0))) {
      cast = true;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {

  }
}
