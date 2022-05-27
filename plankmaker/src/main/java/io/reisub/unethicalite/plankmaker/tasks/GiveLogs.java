package io.reisub.unethicalite.plankmaker.tasks;

import io.reisub.unethicalite.plankmaker.Config;
import io.reisub.unethicalite.plankmaker.PlankMaker;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.DialogOption;
import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.ObjectID;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Dialog;
import net.unethicalite.api.widgets.Widgets;
import net.unethicalite.client.Static;

public class GiveLogs extends Task {
  @Inject private Config config;

  private static final int HOUSE_OPTIONS_WIDGET = 7602250;
  private static final int CALL_SERVANT_WIDGET = 24248339;

  @Override
  public String getStatus() {
    return "Giving logs to butler";
  }

  @Override
  public boolean validate() {
    return Inventory.contains(config.logType().getLogId())
        && Static.getClient().isInInstancedRegion()
        && Static.getClient().getGameState() == GameState.LOGGED_IN
        && TileObjects.getNearest(ObjectID.PORTAL_4525) != null;
  }

  @Override
  public void execute() {
    Time.sleepTick();

    final NPC butler = NPCs.getNearest(Predicates.ids(NpcID.DEMON_BUTLER, NpcID.DEMON_BUTLER_7331));
    if (butler == null || Players.getLocal().distanceTo(butler) > 4) {
      Widget houseOptions = Widgets.fromId(HOUSE_OPTIONS_WIDGET);
      houseOptions.interact(0);

      if (!Time.sleepTicksUntil(() -> Widgets.isVisible(Widgets.fromId(CALL_SERVANT_WIDGET)), 3)) {
        return;
      }

      Widgets.fromId(CALL_SERVANT_WIDGET).interact(0);
      Time.sleepTick();
    }

    if (!Dialog.isOpen()) {
      final NPC calledButler =
          NPCs.getNearest(Predicates.ids(NpcID.DEMON_BUTLER, NpcID.DEMON_BUTLER_7331));
      if (calledButler == null) {
        return;
      }

      GameThread.invoke(() -> calledButler.interact(0));
    }

    if (!Time.sleepTicksUntil(Dialog::isOpen, 10)) {
      return;
    }

    if (Dialog.canContinueNPC()) {
      final Widget npcTextWidget = Widgets.get(WidgetInfo.DIALOG_NPC_TEXT);

      if (npcTextWidget.getText().contains("thy inventory is full")) {
        Dialog.invokeDialog(
            DialogOption.NPC_CONTINUE,
            DialogOption.CHAT_OPTION_ONE,
            DialogOption.PLAYER_CONTINUE,
            DialogOption.NPC_CONTINUE);

        Time.sleepTicks(3);

        Time.sleepTicksUntil(
            () ->
                NPCs.getNearest(Predicates.ids(NpcID.DEMON_BUTLER, NpcID.DEMON_BUTLER_7331))
                    != null,
            10);
        return;
      } else {
        Dialog.invokeDialog(
            DialogOption.NPC_CONTINUE,
            DialogOption.CHAT_OPTION_ONE,
            DialogOption.NPC_CONTINUE,
            DialogOption.CHAT_OPTION_ONE,
            DialogOption.NPC_CONTINUE,
            DialogOption.CHAT_OPTION_ONE,
            DialogOption.NPC_CONTINUE);
      }
    } else {
      Dialog.invokeDialog(
          DialogOption.CHAT_OPTION_ONE,
          DialogOption.NPC_CONTINUE,
          DialogOption.CHAT_OPTION_ONE,
          DialogOption.NPC_CONTINUE);
    }

    PlankMaker.lastGive = Static.getClient().getTickCount();

    Time.sleepTicksUntil(() -> !Inventory.contains(config.logType().getLogId()), 5);
    Time.sleepTicks(3);
  }
}
