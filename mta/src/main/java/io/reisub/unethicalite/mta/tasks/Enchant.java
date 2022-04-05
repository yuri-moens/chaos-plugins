package io.reisub.unethicalite.mta.tasks;

import com.google.common.collect.ImmutableSet;
import dev.hoot.api.entities.Players;
import dev.hoot.api.game.GameThread;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.magic.Magic;
import dev.hoot.api.magic.Regular;
import dev.hoot.bot.managers.Static;
import io.reisub.unethicalite.mta.Mta;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.ItemID;

import java.util.Set;

public class Enchant extends Task {
    private final Set<Integer> ENCHANT_ITEM_IDS = ImmutableSet.of(
            ItemID.PENTAMID,
            ItemID.CUBE,
            ItemID.CYLINDER,
            ItemID.ICOSAHEDRON,
            ItemID.DRAGONSTONE_6903
    );

    private int last;

    @Override
    public String getStatus() {
        return "Casting enchant";
    }

    @Override
    public boolean validate() {
        return Static.getClient().getTickCount() > last + 3
                &&Utils.isInRegion(Mta.MTA_REGION)
                && Players.getLocal().getWorldLocation().getPlane() == 0
                && Inventory.contains(Predicates.ids(ENCHANT_ITEM_IDS));
    }

    @Override
    public void execute() {
        Item item = Inventory.getFirst(Predicates.ids(ENCHANT_ITEM_IDS));
        if (item == null) {
            return;
        }

        GameThread.invoke(() -> Magic.cast(Regular.LVL_6_ENCHANT, item));

        last = Static.getClient().getTickCount();
    }
}
