package io.reisub.unethicalite.barrows.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.bot.managers.Static;
import io.reisub.unethicalite.barrows.Room;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.NullObjectID;

public class OpenChest extends Task {
    @Override
    public String getStatus() {
        return "Opening chest";
    }

    @Override
    public boolean validate() {
        return Room.getCurrentRoom() == Room.C
                && Static.getClient().getHintArrowNpc() == null
                && TileObjects.getNearest(o -> o.getId() == NullObjectID.NULL_20973 && o.hasAction("Open")) != null;
    }

    @Override
    public void execute() {
        TileObjects.getNearest(o -> o.getId() == NullObjectID.NULL_20973 && o.hasAction("Open")).interact("Open");

        Time.sleepTicksUntil(() -> TileObjects.getNearest(o -> o.getId() == NullObjectID.NULL_20973 && o.hasAction("Search")) != null, 20);
    }
}
