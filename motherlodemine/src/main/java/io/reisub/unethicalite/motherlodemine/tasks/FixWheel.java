package io.reisub.unethicalite.motherlodemine.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import io.reisub.unethicalite.motherlodemine.MotherlodeMine;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;

import javax.inject.Inject;

public class FixWheel extends Task {
    @Inject
    private MotherlodeMine plugin;

    @Override
    public String getStatus() {
        return "Fixing wheel";
    }

    @Override
    public boolean validate() {
        return plugin.getCurrentActivity() == Activity.IDLE
                && !plugin.isUpstairs()
                && plugin.getPreviousActivity() == Activity.DEPOSITING
                && TileObjects.getAll(ObjectID.BROKEN_STRUT).size() == 2;
    }

    @Override
    public void execute() {
        TileObject strut = TileObjects.getNearest(ObjectID.BROKEN_STRUT);
        if (strut == null) {
            return;
        }

        plugin.setActivity(Activity.REPAIRING);

        GameThread.invoke(() -> strut.interact("Hammer"));
        Time.sleepTicksUntil(() -> plugin.getCurrentActivity() == Activity.IDLE, 30);
    }
}
