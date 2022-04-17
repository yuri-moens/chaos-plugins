package io.reisub.unethicalite.barrows.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.widgets.Widgets;
import dev.unethicalite.managers.Static;
import io.reisub.unethicalite.barrows.Barrows;
import io.reisub.unethicalite.barrows.Config;
import io.reisub.unethicalite.barrows.Room;
import io.reisub.unethicalite.combathelper.CombatHelper;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.NullObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.WidgetID;

import javax.inject.Inject;

public class SearchChest extends Task {
    @Inject
    private Barrows plugin;

    @Inject
    private Config config;

    @Inject
    private CombatHelper combatHelper;

    private TileObject chest;

    @Override
    public String getStatus() {
        return "Searching chest";
    }

    @Override
    public boolean validate() {
        return Room.getCurrentRoom() == Room.C
                && (Static.getClient().getHintArrowNpc() == null || Static.getClient().getHintArrowNpc().isDead())
                && (Players.getLocal().getInteracting() == null || Players.getLocal().getInteracting().isDead() || plugin.getPotentialWithLastBrother() > config.potential().getMaximum())
                && (chest = TileObjects.getNearest(o -> o.getId() == NullObjectID.NULL_20973 && o.hasAction("Search"))) != null;
    }

    @Override
    public void execute() {
        if (combatHelper.getPrayerHelper().isFlicking()) {
            combatHelper.getPrayerHelper().toggleFlicking();
        }

        GameThread.invoke(() -> chest.interact("Search"));
        Time.sleepTicksUntil(() -> Widgets.isVisible(Widgets.get(WidgetID.BARROWS_REWARD_GROUP_ID, 0)), 10);
    }
}
