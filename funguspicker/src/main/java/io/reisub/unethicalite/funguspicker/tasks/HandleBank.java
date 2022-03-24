package io.reisub.unethicalite.funguspicker.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.items.Bank;
import dev.hoot.api.items.Equipment;
import dev.hoot.api.items.Inventory;
import io.reisub.unethicalite.funguspicker.FungusPicker;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Interact;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.BankTask;
import net.runelite.api.ItemID;

import javax.inject.Inject;
import java.time.Duration;

public class HandleBank extends BankTask {
    @Inject
    private FungusPicker plugin;

    @Override
    public boolean validate() {
        return Inventory.isFull()
                && Inventory.contains(ItemID.MORT_MYRE_FUNGUS)
                && isLastBankDurationAgo(Duration.ofSeconds(5));
    }

    @Override
    public void execute() {
        if (Players.getLocal().getWorldLocation().getRegionID() == FungusPicker.MONASTERY_REGION) {
            boolean interacted = Interact.interactWithInventoryOrEquipment(ItemID.DRAKANS_MEDALLION, "Rub", "Ver Sinhaza", 2);

            if (!interacted) {
                plugin.stop("Couldn't find Drakan's medallion. Stopping plugin.");
                return;
            }

            Time.sleepTicksUntil(() -> FungusPicker.VER_SINHAZA_REGION_IDS.contains(Players.getLocal().getWorldLocation().getRegionID()), 10);
        }

        open();

        Bank.depositAll(ItemID.MORT_MYRE_FUNGUS);

        if (!Inventory.contains(Predicates.ids(Constants.DUELING_RING_IDS))
                && !Equipment.contains(Predicates.ids(Constants.DUELING_RING_IDS))
                && !Inventory.contains(Predicates.ids(Constants.ARDOUGNE_CLOAK_IDS))
                && !Equipment.contains(Predicates.ids(Constants.ARDOUGNE_CLOAK_IDS))) {
            Bank.withdraw(Predicates.ids(Constants.DUELING_RING_IDS), 1, Bank.WithdrawMode.ITEM);
        }
    }
}
