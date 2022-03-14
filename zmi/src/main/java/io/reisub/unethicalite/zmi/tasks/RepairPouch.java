package io.reisub.unethicalite.zmi.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.magic.Lunar;
import dev.hoot.api.magic.Magic;
import dev.hoot.api.widgets.Dialog;
import dev.hoot.api.widgets.DialogOption;
import dev.hoot.api.widgets.Widgets;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ItemID;

public class RepairPouch extends Task {
    @Override
    public String getStatus() {
        return "Repairing pouch";
    }

    @Override
    public boolean validate() {
        return Inventory.contains(Predicates.ids(Constants.DEGRADED_ESSENCE_POUCH_IDS))
                && Inventory.contains(ItemID.COSMIC_RUNE)
                && Players.getLocal().isIdle();
    }

    @Override
    public void execute() {
        Magic.cast(Lunar.NPC_CONTACT);
        Time.sleepTicksUntil(() -> Widgets.isVisible(Widgets.get(75, 12)), 5);

        Widgets.get(75, 12).interact("Dark Mage");
        Time.sleepTicksUntil(Dialog::canContinue, 20);

        Dialog.invokeDialog(
                DialogOption.NPC_CONTINUE,
                DialogOption.CHAT_OPTION_TWO,
                DialogOption.PLAYER_CONTINUE,
                DialogOption.NPC_CONTINUE
        );
    }
}
