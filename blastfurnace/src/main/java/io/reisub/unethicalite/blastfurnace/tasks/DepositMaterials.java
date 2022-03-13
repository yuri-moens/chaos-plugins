package io.reisub.unethicalite.blastfurnace.tasks;

import dev.hoot.api.commons.Rand;
import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.items.Equipment;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.movement.Movement;
import io.reisub.unethicalite.blastfurnace.BlastFurnace;
import io.reisub.unethicalite.blastfurnace.Config;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;

import javax.inject.Inject;

public class DepositMaterials extends Task {
    @Inject
    private BlastFurnace plugin;

    @Inject
    private Config config;

    private static final String CONVEYER_BELT_ACTION = "Put-ore-on";

    @Override
    public String getStatus() {
        return "Depositing materials";
    }

    @Override
    public boolean validate() {
        return plugin.getCurrentActivity() == Activity.IDLE
                && Inventory.isFull()
                && !Inventory.contains(config.metal().getBarId());
    }

    @Override
    public void execute() {
        plugin.setActivity(Activity.DEPOSITING);

        if (!config.useStamina() && Movement.isRunEnabled()) {
            Movement.toggleRun();
        }

        TileObject conveyorBelt = TileObjects.getNearest(ObjectID.CONVEYOR_BELT);
        if (conveyorBelt == null) {
            return;
        }

        Item coalBag = Inventory.getFirst(ItemID.COAL_BAG_12019);

        switch (config.metal()) {
            case STEEL:
            case MITHRIL:
                conveyorBelt.interact(CONVEYER_BELT_ACTION);
                Time.sleepTicksUntil(() -> plugin.getCurrentActivity() == Activity.IDLE, 25);

                plugin.setActivity(Activity.DEPOSITING);

                coalBag.interact("Empty");
                Time.sleepTick();

                conveyorBelt.interact(CONVEYER_BELT_ACTION);
                Time.sleepTicksUntil(() -> plugin.getCurrentActivity() == Activity.IDLE, 5);
                break;
            case GOLD:
                if (!Equipment.contains(ItemID.GOLDSMITH_GAUNTLETS)) {
                    conveyorBelt.interact(CONVEYER_BELT_ACTION);
                    Time.sleepTicks(Rand.nextInt(4, 7));

                    Item gauntlets = Inventory.getFirst(ItemID.GOLDSMITH_GAUNTLETS);
                    if (gauntlets != null) {
                        gauntlets.interact("Wear");
                    }
                }

                conveyorBelt.interact(CONVEYER_BELT_ACTION);
                Time.sleepTicksUntil(() -> plugin.getCurrentActivity() == Activity.IDLE, 25);
                break;
        }

        if (!Movement.isRunEnabled()) {
            Movement.toggleRun();
        }

        if (plugin.isExpectingBars()) {
            Movement.walk(new WorldPoint(1940, 4962, 0));
        }
    }
}
