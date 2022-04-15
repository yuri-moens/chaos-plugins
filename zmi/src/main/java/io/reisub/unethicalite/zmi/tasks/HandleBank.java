package io.reisub.unethicalite.zmi.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.items.Bank;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.utils.api.CBank;
import io.reisub.unethicalite.utils.tasks.BankTask;
import io.reisub.unethicalite.zmi.Config;
import io.reisub.unethicalite.zmi.Pouch;
import io.reisub.unethicalite.zmi.Zmi;
import net.runelite.api.Item;
import net.runelite.api.ItemID;

import javax.inject.Inject;
import java.time.Duration;
import java.util.List;

public class HandleBank extends BankTask {
    @Inject
    private Config config;

    private static final int UNDERGROUND_REGION_ID = 12119;

    @Override
    public boolean validate() {
        return Players.getLocal().getWorldLocation().getRegionID() == UNDERGROUND_REGION_ID
                && !Inventory.contains(ItemID.PURE_ESSENCE, ItemID.DAEYALT_ESSENCE)
                && isLastBankDurationAgo(Duration.ofSeconds(5))
                && Players.getLocal().distanceTo(Zmi.NEAR_ALTAR) > 10;
    }

    @Override
    public void execute() {
        Time.sleepTick();

        if (!open("Eniola")) {
            return;
        }

        CBank.depositAllExcept(
                false,
                ItemID.GIANT_POUCH,
                ItemID.GIANT_POUCH_5515,
                ItemID.LARGE_POUCH,
                ItemID.LARGE_POUCH_5513,
                ItemID.MEDIUM_POUCH,
                ItemID.MEDIUM_POUCH_5511,
                ItemID.SMALL_POUCH,
                ItemID.RUNE_POUCH,
                ItemID.MIND_RUNE
        );

        if (config.useStamina()
                && isStaminaExpiring(Duration.ofSeconds(25))) {
            drinkStamina();
        }

        int essenceId = Bank.contains(ItemID.DAEYALT_ESSENCE) ? ItemID.DAEYALT_ESSENCE : ItemID.PURE_ESSENCE;

        Bank.withdrawAll(essenceId, Bank.WithdrawMode.ITEM);
        Time.sleepTicksUntil(Inventory::isFull, 3);

        Item giantPouch = Bank.Inventory.getFirst(ItemID.GIANT_POUCH);
        if (giantPouch != null) {
            Zmi.pouchesAreEmpty = false;

            CBank.bankInventoryInteract(giantPouch, "Fill");
            Pouch.GIANT.addHolding(Pouch.GIANT.getHoldAmount());

            Bank.withdrawAll(essenceId, Bank.WithdrawMode.ITEM);
            Time.sleepTicksUntil(Inventory::isFull, 3);
        }

        List<Item> pouches = Bank.Inventory.getAll(ItemID.SMALL_POUCH, ItemID.MEDIUM_POUCH, ItemID.LARGE_POUCH);
        if (!pouches.isEmpty()) {
            Zmi.pouchesAreEmpty = false;

            for (Item pouch : pouches) {
                CBank.bankInventoryInteract(pouch, "Fill");

                Pouch pouchEnum = Pouch.forItem(pouch.getId());
                if (pouchEnum != null) {
                    pouchEnum.addHolding(pouchEnum.getHoldAmount());
                }
            }

            Bank.withdrawAll(essenceId, Bank.WithdrawMode.ITEM);
        }
    }
}
