package io.reisub.unethicalite.barrows.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.widgets.Widgets;
import dev.hoot.bot.managers.Static;
import io.reisub.unethicalite.barrows.Barrows;
import io.reisub.unethicalite.barrows.Room;
import io.reisub.unethicalite.combathelper.CombatHelper;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.NullObjectID;
import net.runelite.api.widgets.WidgetID;

import javax.inject.Inject;

public class SearchChest extends Task {
    @Inject
    private Barrows plugin;

    @Inject
    private CombatHelper combatHelper;

    @Override
    public String getStatus() {
        return "Searching chest";
    }

    @Override
    public boolean validate() {
        return Room.getCurrentRoom() == Room.C
                && Static.getClient().getHintArrowNpc() == null
                && Players.getLocal().getInteracting() == null
                && TileObjects.getNearest(o -> o.getId() == NullObjectID.NULL_20973 && o.hasAction("Search")) != null;
    }

    @Override
    public void execute() {
        if (combatHelper.getPrayerHelper().isFlicking()) {
            combatHelper.getPrayerHelper().toggleFlicking();
        }

        TileObjects.getNearest(o -> o.getId() == NullObjectID.NULL_20973 && o.hasAction("Search")).interact("Search");
        Time.sleepTicksUntil(() -> Widgets.isVisible(Widgets.get(WidgetID.BARROWS_REWARD_GROUP_ID, 0)), 10);
    }
}