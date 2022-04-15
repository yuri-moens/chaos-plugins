package io.reisub.unethicalite.barrows.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.widgets.Widgets;
import dev.unethicalite.managers.Static;
import io.reisub.unethicalite.barrows.Barrows;
import io.reisub.unethicalite.barrows.Room;
import io.reisub.unethicalite.combathelper.CombatHelper;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.WidgetInfo;

import javax.inject.Inject;
import java.util.Queue;

public class NavigateTunnel extends Task {
    @Inject
    private Barrows plugin;

    @Inject
    private CombatHelper combatHelper;

    private Room navigatingFrom;
    private Room navigatingTo;

    @Override
    public String getStatus() {
        return "Navigating tunnel";
    }

    @Override
    public boolean validate() {
        if (!Utils.isInRegion(Barrows.CRYPT_REGION) || Players.getLocal().getWorldLocation().getPlane() != 0) {
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

            System.out.println(plugin.getTunnelPath());
        }

        if (navigatingFrom == null || navigatingTo == current) {
            navigatingFrom = plugin.getTunnelPath().poll();
            navigatingTo = plugin.getTunnelPath().peek();
        }

        return Static.getClient().getHintArrowNpc() == null
                && Players.getLocal().getInteracting() == null
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

        GameThread.invoke(() -> door.interact("Open"));

        if (Room.isInCorridor()) {
            Time.sleepTicksUntil(() -> Room.getCurrentRoom() == navigatingTo, 20);
        } else {
            if (navigatingTo == Room.C) {
                Time.sleepTicksUntil(() -> Widgets.isVisible(Widgets.get(WidgetInfo.BARROWS_FIRST_PUZZLE)), 20);
            } else {
                Time.sleepTicksUntil(Room::isInCorridor, 20);
            }
        }

        Time.sleepTick();
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
}
