package io.reisub.unethicalite.birdhouse.tasks;

import io.reisub.unethicalite.birdhouse.BirdHouse;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Widgets;

@AllArgsConstructor
public class GetTools extends Task {
  private static final Supplier<Widget> DIBBER = () -> Widgets.get(BirdHouse.TOOL_WIDGET_ID, 9);
  private static final Supplier<Widget> SPADE = () -> Widgets.get(BirdHouse.TOOL_WIDGET_ID, 10);
  private static final Supplier<Widget> BOTTOMLESS_BUCKET =
      () -> Widgets.get(BirdHouse.TOOL_WIDGET_ID, 15);
  private static final Supplier<Widget> COMPOST = () -> Widgets.get(BirdHouse.TOOL_WIDGET_ID, 17);
  private static final Supplier<Widget> SUPERCOMPOST =
      () -> Widgets.get(BirdHouse.TOOL_WIDGET_ID, 18);
  private static final Supplier<Widget> ULTRACOMPOST =
      () -> Widgets.get(BirdHouse.TOOL_WIDGET_ID, 19);

  private final BirdHouse plugin;

  @Override
  public String getStatus() {
    return "Getting farming tools";
  }

  @Override
  public boolean validate() {
    return plugin.isUnderwater() && !Inventory.contains(ItemID.SEED_DIBBER);
  }

  @Override
  public void execute() {
    Time.sleepTick();
    NPC leprechaun = NPCs.getNearest("Tool Leprechaun");
    if (leprechaun == null) {
      return;
    }

    GameThread.invoke(() -> leprechaun.interact("Exchange"));
    Time.sleepTicksUntil(() -> Widgets.isVisible(BirdHouse.TOOLS.get()), 30);
    Time.sleepTick();

    DIBBER.get().interact("Remove-1");

    final boolean needSpade = TileObjects.getNearest("Dead seaweed") != null;

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
