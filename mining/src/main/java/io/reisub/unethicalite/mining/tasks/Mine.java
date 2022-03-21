package io.reisub.unethicalite.mining.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.movement.Movement;
import dev.hoot.bot.managers.Static;
import io.reisub.unethicalite.mining.Config;
import io.reisub.unethicalite.mining.RockPosition;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.GameState;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class Mine extends Task {
    @Inject
    private Config config;

    private final AtomicInteger ticks = new AtomicInteger(0);

    private LinkedList<RockPosition> rockPositions;
    private RockPosition currentRockPosition;

    @Override
    public String getStatus() {
        return "Mining";
    }

    @Override
    public boolean validate() {
        return !Inventory.isFull()
                && isReady();
    }

    @Override
    public void execute() {
        if (!Movement.isRunEnabled()) {
            Movement.toggleRun();
        }

        TileObject rock = getRock();

        if (config.location().isThreeTick()) {
            Item knife = Inventory.getFirst(ItemID.KNIFE);
            Item logs = Inventory.getFirst(ItemID.TEAK_LOGS, ItemID.MAHOGANY_LOGS);

            if (knife == null || logs == null) {
                return;
            }

            knife.useOn(logs);
        }

        if (rock == null && currentRockPosition != null) {
            Movement.walk(currentRockPosition.getInteractFrom());
        }

        while (rock == null && config.location().isThreeTick()) {
            rock = TileObjects.getFirstAt(currentRockPosition.getRock(), Predicates.ids(config.location().getRockIds()));
        }

        if (rock == null || Inventory.isFull()) {
            return;
        }

        rock.interact(0);

        if (!config.location().isThreeTick()) {
            Time.sleepTicksUntil(() -> !Players.getLocal().isIdle(), 10);
        }
    }

    @Subscribe
    private void onGameTick(GameTick event) {
        ticks.incrementAndGet();
    }

    @Subscribe
    private void onItemContainerChanged(ItemContainerChanged event) {
        if (Static.getClient().getGameState() != GameState.LOGGED_IN) {
            return;
        }

        if (Inventory.isFull() && rockPositions != null) {
            resetQueue();
        }
    }

    private boolean isReady() {
        if (config.location().isThreeTick()) {
            return ticks.get() % 3 == 0;
        } else {
            return Players.getLocal().isIdle();
        }
    }

    private TileObject getRock() {
        if (config.location().getRockPositions() != null && rockPositions == null) {
            resetQueue();
        }

        if (rockPositions == null || rockPositions.isEmpty()) {
            return null;
        } else {
            currentRockPosition = rockPositions.poll();
            rockPositions.add(currentRockPosition);

            return TileObjects.getFirstAt(currentRockPosition.getRock(), Predicates.ids(config.location().getRockIds()));
        }
    }

    private void resetQueue() {
        rockPositions = new LinkedList<>();
        rockPositions.addAll(config.location().getRockPositions());
    }
}
