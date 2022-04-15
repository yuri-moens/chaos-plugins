package io.reisub.unethicalite.birdhouse.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.NPCs;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.widgets.Widgets;
import io.reisub.unethicalite.birdhouse.BirdHouse;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.AllArgsConstructor;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.widgets.Widget;

import java.util.function.Supplier;

@AllArgsConstructor
public class GetTools extends Task {
    private static final Supplier<Widget> DIBBER = () -> Widgets.get(BirdHouse.TOOL_WIDGET_ID, 9);
    private static final Supplier<Widget> SPADE = () -> Widgets.get(BirdHouse.TOOL_WIDGET_ID, 10);
    private static final Supplier<Widget> BOTTOMLESS_BUCKET = () -> Widgets.get(BirdHouse.TOOL_WIDGET_ID, 15);
    private static final Supplier<Widget> COMPOST = () -> Widgets.get(BirdHouse.TOOL_WIDGET_ID, 17);
    private static final Supplier<Widget> SUPERCOMPOST = () -> Widgets.get(BirdHouse.TOOL_WIDGET_ID, 18);
    private static final Supplier<Widget> ULTRACOMPOST = () -> Widgets.get(BirdHouse.TOOL_WIDGET_ID, 19);

    private final BirdHouse plugin;

    @Override
    public String getStatus() {
        return "Getting farming tools";
    }

    @Override
    public boolean validate() {
        return plugin.isUnderwater()
                && !Inventory.contains(ItemID.SEED_DIBBER);
    }

    @Override
    public void execute() {
        Time.sleepTick();
        NPC leprechaun = NPCs.getNearest("Tool Leprechaun");
        if (leprechaun == null) {
            return;
        }

        boolean needSpade = TileObjects.getNearest("Dead seaweed") != null;

        GameThread.invoke(() -> leprechaun.interact("Exchange"));
        Time.sleepTicksUntil(() -> Widgets.isVisible(BirdHouse.TOOLS.get()), 30);
        Time.sleepTick();

        DIBBER.get().interact("Remove-1");

        if (needSpade) {
            SPADE.get().interact("Remove-1");
        }

        if (getCompostCount(BOTTOMLESS_BUCKET.get()) == 1) {
            BOTTOMLESS_BUCKET.get().interact(0);
        } else if (getCompostCount(ULTRACOMPOST.get()) >= 2) {
            ULTRACOMPOST.get().interact(0);
            ULTRACOMPOST.get().interact(0);
        } else if (getCompostCount(SUPERCOMPOST.get()) >= 2) {
            SUPERCOMPOST.get().interact(0);
            SUPERCOMPOST.get().interact(0);
        } else if (getCompostCount(COMPOST.get()) >= 2) {
            COMPOST.get().interact(0);
            COMPOST.get().interact(0);
        }

        BirdHouse.CLOSE.get().interact("Close");
        Time.sleepTicksUntil(() -> Inventory.contains(ItemID.SEED_DIBBER), 5);
    }

    private int getCompostCount(Widget compostWidget) {
        Widget child = compostWidget.getChild(10);
        if (child == null || child.getText() == null) {
            return 0;
        }

        return Integer.parseInt(child.getText().split("/")[0]);
    }
}
