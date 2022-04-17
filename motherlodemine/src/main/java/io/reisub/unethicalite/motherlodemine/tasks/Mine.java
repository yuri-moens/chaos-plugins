package io.reisub.unethicalite.motherlodemine.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.coords.RectangularArea;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.motherlodemine.MotherlodeMine;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;

public class Mine extends Task {
    @Inject
    private MotherlodeMine plugin;

    private static final RectangularArea MINING_AREA = new RectangularArea(
            new WorldPoint(3747, 5676, 0),
            new WorldPoint(3754, 5684, 0)
    );

    private TileObject oreVein;

    @Override
    public String getStatus() {
        return "Mining";
    }

    @Override
    public boolean validate() {
        if (plugin.getCurrentActivity() != Activity.IDLE) {
            return false;
        }

        oreVein = TileObjects.getNearest(
                o -> o.getName().equals("Ore vein")
                        && o.hasAction("Mine")
                        && MINING_AREA.contains(o.getWorldLocation())
                        && !o.getWorldLocation().equals(new WorldPoint(3764, 5665, 0))
        );

        return !Inventory.isFull()
                && plugin.isUpstairs()
                && oreVein != null;
    }

    @Override
    public void execute() {
        GameThread.invoke(() -> oreVein.interact("Mine"));
        Time.sleepTicksUntil(() -> plugin.getCurrentActivity() == Activity.MINING, 15);
    }

    @Subscribe
    private void onGameTick(GameTick event) {
        if (plugin.isRunning() && plugin.getCurrentActivity() == Activity.MINING) {
            if (oreVein == null) {
                plugin.setActivity(Activity.IDLE);
            } else {
                TileObject oreVeinCheck = TileObjects.getFirstAt(oreVein.getWorldLocation(), o -> o.hasAction("Mine"));
                if (oreVeinCheck == null) {
                    oreVein = null;
                    plugin.setActivity(Activity.IDLE);
                }
            }
        }
    }
}
