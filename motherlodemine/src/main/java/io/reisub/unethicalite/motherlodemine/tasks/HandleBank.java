package io.reisub.unethicalite.motherlodemine.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.motherlodemine.MotherlodeMine;
import io.reisub.unethicalite.utils.api.CBank;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.BankTask;
import net.runelite.api.ItemID;

import javax.inject.Inject;

public class HandleBank extends BankTask {
    @Inject
    private MotherlodeMine plugin;

    @Override
    public boolean validate() {
        return plugin.getCurrentActivity() == Activity.IDLE
                && !plugin.isUpstairs()
                && Inventory.contains(
                        ItemID.RUNITE_ORE,
                        ItemID.ADAMANTITE_ORE,
                        ItemID.MITHRIL_ORE,
                        ItemID.GOLD_ORE,
                        ItemID.COAL,
                        ItemID.UNCUT_SAPPHIRE,
                        ItemID.UNCUT_EMERALD,
                        ItemID.UNCUT_RUBY,
                        ItemID.UNCUT_DIAMOND,
                        ItemID.UNCUT_DRAGONSTONE
                );
    }

    @Override
    public void execute() {
        open();

        CBank.depositAllExcept(false, ItemID.IMCANDO_HAMMER, ItemID.HAMMER, ItemID.GOLDEN_NUGGET);
        Time.sleepTicksUntil(() -> !Inventory.contains(
                ItemID.RUNITE_ORE,
                ItemID.ADAMANTITE_ORE,
                ItemID.MITHRIL_ORE,
                ItemID.GOLD_ORE,
                ItemID.COAL,
                ItemID.UNCUT_SAPPHIRE,
                ItemID.UNCUT_EMERALD,
                ItemID.UNCUT_RUBY,
                ItemID.UNCUT_DIAMOND,
                ItemID.UNCUT_DRAGONSTONE
        ), 3);
    }
}
