package io.reisub.unethicalite.funguspicker.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.game.Skills;
import dev.hoot.api.items.Equipment;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.widgets.Dialog;
import io.reisub.unethicalite.funguspicker.FungusPicker;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.CMovement;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.ObjectID;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;

import javax.inject.Inject;

public class RestorePrayer extends Task {
    @Inject
    private FungusPicker plugin;

    @Override
    public String getStatus() {
        return "Restoring prayer points";
    }

    @Override
    public boolean validate() {
        int regionId = Players.getLocal().getWorldLocation().getRegionID();

        return Skills.getBoostedLevel(Skill.PRAYER) < Skills.getLevel(Skill.PRAYER)
                && (Inventory.isFull() || Skills.getBoostedLevel(Skill.PRAYER) == 0);
    }

    @Override
    public void execute() {
        WorldPoint current = Players.getLocal().getWorldLocation();

        Item cape = Inventory.getFirst(Predicates.ids(Constants.ARDOUGNE_CLOAK_IDS));
        if (cape == null) {
            cape = Equipment.getFirst(Predicates.ids(Constants.ARDOUGNE_CLOAK_IDS));
        }

        if (cape == null) {
            Item duelingRing = Inventory.getFirst(Predicates.ids(Constants.DUELING_RING_IDS));
            if (duelingRing == null) {
                duelingRing = Equipment.getFirst(Predicates.ids(Constants.DUELING_RING_IDS));
                if (duelingRing == null) {
                    plugin.stop("No dueling ring or Ardougne cloak found. Stopping script.");
                    return;
                }

                duelingRing.interact("Ferox enclave");
            }

            duelingRing.interact("Rub");
            Time.sleepTicksUntil(Dialog::isViewingOptions, 5);
            Dialog.chooseOption(3);
        } else {
            cape.interact("Monastery");
        }

        Time.sleepTicksUntil(() -> !Players.getLocal().getWorldLocation().equals(current), 10);

        if (Players.getLocal().getWorldLocation().getRegionID() == FungusPicker.MONASTERY_REGION) {
            TileObjects.getNearest(ObjectID.ALTAR).interact(0); // TODO: id

            Time.sleepTicksUntil(() -> Skills.getBoostedLevel(Skill.PRAYER) == Skills.getLevel(Skill.PRAYER), 20);
        } else {
            CMovement.walkTo(new WorldPoint(0, 0, 0));

            TileObjects.getNearest(ObjectID.POOL_OF_REFRESHMENT).interact(0); // TODO: id

            Time.sleepTicksUntil(() -> Skills.getBoostedLevel(Skill.PRAYER) == Skills.getLevel(Skill.PRAYER), 20);
            Time.sleepTicks(3);
        }
    }
}
