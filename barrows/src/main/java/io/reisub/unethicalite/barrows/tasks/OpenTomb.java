package io.reisub.unethicalite.barrows.tasks;

import io.reisub.unethicalite.barrows.Barrows;
import io.reisub.unethicalite.barrows.Brother;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.DialogOption;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.widgets.Dialog;
import net.unethicalite.client.Static;

public class OpenTomb extends Task {
  @Inject private Barrows plugin;

  @Override
  public String getStatus() {
    return "Opening tomb";
  }

  @Override
  public boolean validate() {
    Brother brother = Brother.getBrotherByCrypt();

    return brother != null && !brother.isDead() && Static.getClient().getHintArrowNpc() == null;
  }

  @Override
  public int execute() {
    plugin.setNewRun(false);

    TileObject sarcophagus = TileObjects.getNearest(Predicates.ids(Barrows.SARCOPHAGUS_IDS));
    if (sarcophagus == null) {
      return 1;
    }

    sarcophagus.interact(0);

    Time.sleepTicksUntil(() -> Dialog.isOpen() || Static.getClient().getHintArrowNpc() != null, 20);

    if (Dialog.isOpen()) {
      plugin.getCurrentBrother().setInTunnel(true);

      boolean isLastBrother = true;

      for (Brother brother : Brother.values()) {
        if (!brother.isDead() && !brother.isInTunnel()) {
          isLastBrother = false;
        }
      }

      if (isLastBrother) {
        Dialog.invokeDialog(DialogOption.PLAIN_CONTINUE, DialogOption.CHAT_OPTION_ONE);

        Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().getPlane() != 3, 5);
      } else {
        TileObject stairs = TileObjects.getNearest(Predicates.ids(Barrows.STAIRCASE_IDS));
        if (stairs == null) {
          return 1;
        }

        stairs.interact(0);
        Time.sleepTicksUntil(() -> Utils.isInRegion(Barrows.BARROWS_REGION), 10);
      }
    } else {
      if (plugin.getCurrentBrother() != Brother.KARIL) {
        Movement.walk(plugin.getCurrentBrother().getPointNextToStairs());
      }
    }

    return 1;
  }
}
