package io.reisub.unethicalite.barrows.tasks;

import io.reisub.unethicalite.barrows.Barrows;
import io.reisub.unethicalite.barrows.Config;
import io.reisub.unethicalite.barrows.Room;
import io.reisub.unethicalite.combathelper.CombatHelper;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.Queue;
import javax.inject.Inject;
import net.runelite.api.TileObject;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.widgets.Widgets;
import net.unethicalite.client.Static;

public class NavigateTunnel extends Task {
  @Inject private Barrows plugin;

  @Inject private Config config;

  @Inject private CombatHelper combatHelper;

  private Room navigatingFrom;
  private Room navigatingTo;
  private boolean stuck;

  @Override
  public String getStatus() {
    return "Navigating tunnel";
  }

  @Override
  public boolean validate() {
    if (!Utils.isInRegion(Barrows.CRYPT_REGION)
        || Players.getLocal().getWorldLocation().getPlane() != 0) {
      return false;
    }

    Room current = Room.getCurrentRoom();

    if (current == Room.C) {
      return false;
    }

    if (plugin.getTunnelPath() == null || plugin.getTunnelPath().isEmpty()) {
      Queue<Room> path = Room.findPath();
      plugin.setTunnelPath(path);

      navigatingFrom = null;
      navigatingTo = null;

      if (plugin.getTunnelPath().isEmpty()) {
        return false;
      }
    }

    if (navigatingFrom == null || navigatingTo == current) {
      navigatingFrom = plugin.getTunnelPath().poll();
      navigatingTo = plugin.getTunnelPath().peek();
    }

    return (Static.getClient().getHintArrowNpc() == null
            || Static.getClient().getHintArrowNpc().isDead())
        && (Players.getLocal().getInteracting() == null
            || Players.getLocal().getInteracting().isDead()
            || plugin.getPotentialWithLastBrother() > config.potential().getMaximum())
        && navigatingTo != null;
  }

  @Override
  public void execute() {
    if (combatHelper.getPrayerHelper().isFlicking()) {
      combatHelper.getPrayerHelper().toggleFlicking();
    }

    TileObject door = getDoor();
    if (door == null) {
      return;
    }

    if (stuck) {
      door = TileObjects.getNearest(Room.DOOR_PREDICATE);
      stuck = false;
    }

    final TileObject finalDoor = door;

    GameThread.invoke(() -> finalDoor.interact("Open"));

    if (!Time.sleepTicksUntil(() -> Players.getLocal().isMoving(), 3)) {
      return;
    }

    if (Room.isInCorridor()) {
      Time.sleepTicksUntil(
          () -> Room.getCurrentRoom() == navigatingTo || !Players.getLocal().isMoving(), 30);
    } else {
      if (navigatingTo == Room.C) {
        Time.sleepTicksUntil(
            () ->
                Widgets.isVisible(Widgets.get(WidgetInfo.BARROWS_FIRST_PUZZLE))
                    || !Players.getLocal().isMoving(),
            30);
      } else {
        Time.sleepTicksUntil(() -> Room.isInCorridor() || !Players.getLocal().isMoving(), 30);
      }
    }
  }

  public TileObject getDoor() {
    Room roomOne;
    Room roomTwo;

    if (Room.isInCorridor()) {
      roomOne = navigatingTo;
      roomTwo = navigatingFrom;
    } else {
      roomOne = navigatingFrom;
      roomTwo = navigatingTo;
    }

    if (roomOne.getNorth() == roomTwo) {
      return roomOne.getNorthDoor();
    } else if (roomOne.getEast() == roomTwo) {
      return roomOne.getEastDoor();
    } else if (roomOne.getSouth() == roomTwo) {
      return roomOne.getSouthDoor();
    } else if (roomOne.getWest() == roomTwo) {
      return roomOne.getWestDoor();
    }

    return null;
  }

  @Subscribe
  private void onChatMessage(ChatMessage event) {
    if (plugin.isRunning() && event.getMessage().contains("I can't reach that!")) {
      stuck = true;
    }
  }
}
