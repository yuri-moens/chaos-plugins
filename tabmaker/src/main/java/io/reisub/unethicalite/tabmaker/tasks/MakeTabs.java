package io.reisub.unethicalite.tabmaker.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.magic.Magic;
import dev.unethicalite.api.magic.SpellBook.Standard;
import dev.unethicalite.api.widgets.Widgets;
import dev.unethicalite.client.Static;
import io.reisub.unethicalite.tabmaker.Config;
import io.reisub.unethicalite.tabmaker.TabMaker;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.Widget;

public class MakeTabs extends Task {

  @Inject
  private TabMaker plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Making tabs";
  }

  @Override
  public boolean validate() {
    return Inventory.contains(ItemID.SOFT_CLAY)
        && plugin.getCurrentActivity() == Activity.IDLE;
  }

  @Override
  public void execute() {
    if (!Static.getClient().isInInstancedRegion()) {
      Magic.cast(Standard.TELEPORT_TO_HOUSE);

      Time.sleepTicksUntil(() -> Static.getClient().isInInstancedRegion(), 10);
      Time.sleepTicks(2);
    }

    final TileObject lectern = TileObjects.getNearest("Lectern");

    if (lectern == null) {
      return;
    }

    lectern.interact(0);

    Time.sleepTicksUntil(() -> Widgets.isVisible(Widgets.get(79, 0)), 50);

    final Widget tabWidget = Widgets.get(79, config.tab().getId());

    if (tabWidget == null) {
      return;
    }

    plugin.setActivity(Activity.MAKING_TABS);
    tabWidget.interact(0);
  }
}