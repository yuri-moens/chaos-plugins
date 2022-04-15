package io.reisub.unethicalite.birdhouse.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.NPCs;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.items.Bank;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.widgets.Widgets;
import io.reisub.unethicalite.birdhouse.BirdHouse;
import io.reisub.unethicalite.utils.tasks.BankTask;
import lombok.AllArgsConstructor;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.Widget;

import java.util.function.Supplier;

@AllArgsConstructor
public class Deposit extends BankTask {
    private static final Supplier<Widget> DIBBER = () -> Widgets.get(BirdHouse.TOOL_WIDGET_ID + 1, 2);
    private static final Supplier<Widget> SPADE = () -> Widgets.get(BirdHouse.TOOL_WIDGET_ID + 1, 3);
    private static final Supplier<Widget> BOTTOMLESS_BUCKET = () -> Widgets.get(BirdHouse.TOOL_WIDGET_ID + 1, 8);
    private static final Supplier<Widget> BUCKET = () -> Widgets.get(BirdHouse.TOOL_WIDGET_ID + 1, 9);

    private final BirdHouse plugin;

    @Override
    public String getStatus() {
        return "Depositing everything";
    }

    @Override
    public boolean validate() {
        return plugin.isUnderwater()
                && TileObjects.getAll("Seaweed").size() == 2;
    }

    @Override
    public void execute() {
        NPC leprechaun = NPCs.getNearest("Tool Leprechaun");
        if (leprechaun == null) {
            return;
        }

        GameThread.invoke(() -> leprechaun.interact("Exchange"));
        Time.sleepTicksUntil(() -> Widgets.isVisible(BirdHouse.TOOLS.get()), 30);

        DIBBER.get().interact("Store-1");

        if (Inventory.contains(ItemID.SPADE)) {
            SPADE.get().interact("Store-1");
        }

        if (Inventory.contains(ItemID.BUCKET)) {
            BUCKET.get().interact("Store-5");
        }

        if (Inventory.contains(ItemID.BOTTOMLESS_COMPOST_BUCKET, ItemID.BOTTOMLESS_COMPOST_BUCKET_22997)) {
            BOTTOMLESS_BUCKET.get().interact("Store");
        }

        TileObject rope = TileObjects.getNearest("Anchor rope");
        if (rope == null) {
            return;
        }

        GameThread.invoke(() -> rope.interact("Climb"));
        Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().getRegionID() == 14908, 30);

        Inventory.getAll((i) -> i.hasAction("Wear")).forEach((i) -> i.interact("Wear"));
        Inventory.getAll((i) -> i.hasAction("Wield")).forEach((i) -> i.interact("Wield"));

        open();

        Bank.depositInventory();

        close();
    }
}
