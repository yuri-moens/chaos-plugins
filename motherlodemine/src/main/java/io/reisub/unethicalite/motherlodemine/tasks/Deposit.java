package io.reisub.unethicalite.motherlodemine.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.coords.RectangularArea;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.motherlodemine.MotherlodeMine;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;

import javax.inject.Inject;

public class Deposit extends Task {
    @Inject
    private MotherlodeMine plugin;

    private static final RectangularArea HOPPER_AREA = new RectangularArea(
            new WorldPoint(3741, 5657, 0),
            new WorldPoint(3755, 5674, 0)
    );

    private TileObject hopper;

    @Override
    public String getStatus() {
        return "Depositing in hopper";
    }

    @Override
    public boolean validate() {
        return plugin.getCurrentActivity() == Activity.IDLE
                && !plugin.isUpstairs()
                && Inventory.contains(ItemID.PAYDIRT)
                && (hopper = TileObjects.getNearest("Hopper")) != null;
    }

    @Override
    public void execute() {
        plugin.setActivity(Activity.DEPOSITING);

        GameThread.invoke(() -> hopper.interact("Deposit"));
        Time.sleepTicksUntil(() -> plugin.getCurrentActivity() == Activity.IDLE, 15);
        Time.sleepTicks(2);
    }
}
