package io.reisub.unethicalite.superglassmake.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.items.Bank;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.magic.Lunar;
import dev.hoot.api.magic.Magic;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ItemID;

public class CastSuperglassMake extends Task {
    @Override
    public String getStatus() {
        return "Casting Superglass Make";
    }

    @Override
    public boolean validate() {
        return Inventory.contains(ItemID.BUCKET_OF_SAND)
                && Inventory.contains(ItemID.ASTRAL_RUNE)
                && Inventory.contains(ItemID.SODA_ASH, ItemID.GIANT_SEAWEED)
                && !Bank.isOpen();
    }

    @Override
    public void execute() {
        Magic.cast(Lunar.SUPERGLASS_MAKE);
        Time.sleepTicksUntil((() -> Inventory.contains(ItemID.MOLTEN_GLASS)), 5);
        Time.sleepTicks(3);
    }
}
