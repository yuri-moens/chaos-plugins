package io.reisub.unethicalite.barrows.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.movement.Movement;
import dev.unethicalite.api.widgets.Prayers;
import dev.unethicalite.managers.Static;
import io.reisub.unethicalite.barrows.Barrows;
import io.reisub.unethicalite.barrows.Brother;
import io.reisub.unethicalite.barrows.Room;
import io.reisub.unethicalite.combathelper.CombatHelper;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.coords.WorldPoint;

import javax.inject.Inject;

public class FightBrother extends Task {
    @Inject
    private Barrows plugin;

    @Inject
    private CombatHelper combatHelper;

    @Override
    public String getStatus() {
        return "Fighting brother";
    }

    @Override
    public boolean validate() {
        return Utils.isInRegion(Barrows.CRYPT_REGION)
                && Players.getLocal().getInteracting() == null
                && Static.getClient().getHintArrowNpc() != null;
    }

    @Override
    public void execute() {
        if (!combatHelper.getPrayerHelper().isFlicking()) {
            switch (plugin.getCurrentBrother()) {
                case DHAROK:
                case AHRIM:
                case KARIL:
                    combatHelper.getPrayerHelper().toggleFlicking();
                    break;
                case TORAG:
                case VERAC:
                case GUTHAN:
                    if (Prayers.getPoints() > 0) {
                        combatHelper.getPrayerHelper().toggleFlicking();
                    }
            }
        }

        GameThread.invoke(() -> Static.getClient().getHintArrowNpc().interact("Attack"));
        Time.sleepTicksUntil(() -> Players.getLocal().getInteracting() != null, 3);

        Brother currentBrother = plugin.getCurrentBrother();
        WorldPoint currentLocation = Players.getLocal().getWorldLocation();

        if (currentBrother != Brother.KARIL
                && currentLocation.getPlane() == 3
                && !currentLocation.equals(currentBrother.getPointNextToStairs())
                && !Static.getClient().getHintArrowNpc().getWorldLocation().equals(currentBrother.getPointNextToStairs())) {
            Time.sleepTicks(2);
            Movement.walk(currentBrother.getPointNextToStairs());
            Time.sleepTicks(1);
        }

        if (currentLocation.getPlane() == 0) {
            if (currentBrother == Brother.DHAROK || currentBrother == Brother.TORAG || currentBrother == Brother.VERAC || currentBrother == Brother.GUTHAN) {
                Room currentRoom = Room.getCurrentRoom();
                if (currentRoom == null) {
                    return;
                }

                WorldPoint ladderTile = Room.getCurrentRoom().getLadderTile();

                if (ladderTile != null && currentLocation.distanceTo(ladderTile) > 1) {
                    WorldPoint target;
                    if (Math.abs(currentLocation.getY() - ladderTile.getY()) <= 3) {
                        target = currentLocation.getX() > ladderTile.getX() ? ladderTile.dx(-1) : ladderTile.dx(1);
                    } else {
                        target = currentLocation.getY() > ladderTile.getY() ? ladderTile.dy(-1) : ladderTile.dy(1);
                    }

                    Movement.walk(target);
                    Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().equals(target), 10);
                }
            }
        }
    }
}
