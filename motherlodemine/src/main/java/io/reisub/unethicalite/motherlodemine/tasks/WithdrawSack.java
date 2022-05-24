package io.reisub.unethicalite.motherlodemine.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.motherlodemine.MotherlodeMine;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;

public class WithdrawSack extends Task {
  @Inject private MotherlodeMine plugin;

  @Override
  public String getStatus() {
    return "Withdrawing from sack";
  }

  @Override
  public boolean validate() {
    return plugin.getCurrentActivity() == Activity.IDLE
        && !plugin.isUpstairs()
        && plugin.isSackFull()
        && !Inventory.contains(
            ItemID.PAYDIRT,
            ItemID.RUNITE_ORE,
            ItemID.ADAMANTITE_ORE,
            ItemID.MITHRIL_ORE,
            ItemID.GOLD_ORE,
            ItemID.COAL,
            ItemID.UNCUT_SAPPHIRE,
            ItemID.UNCUT_EMERALD,
            ItemID.UNCUT_RUBY,
            ItemID.UNCUT_DIAMOND,
            ItemID.UNCUT_DRAGONSTONE);
  }

  @Override
  public void execute() {
    TileObject sack =
        TileObjects.getNearest(o -> o.getName().equals("Sack") && o.hasAction("Search"));
    if (sack == null) {
      return;
    }

    plugin.setActivity(Activity.WITHDRAWING);

    GameThread.invoke(() -> sack.interact("Search"));
    Time.sleepTicksUntil(() -> plugin.getCurrentActivity() == Activity.IDLE, 30);
  }
}
