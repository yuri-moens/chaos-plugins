package io.reisub.unethicalite.secondarygatherer.tasks;

import io.reisub.unethicalite.secondarygatherer.Config;
import io.reisub.unethicalite.secondarygatherer.Secondary;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.TileItem;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.entities.TileItems;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.magic.SpellBook.Standard;
import net.unethicalite.client.Static;

public class GrabWine extends Task {

  @Inject
  private Config config;

  private TileItem wine;
  private int last;

  @Override
  public String getStatus() {
    return "Grabbing Wine of Zamorak";
  }

  @Override
  public boolean validate() {
    if (config.secondary() != Secondary.WINE_OF_ZAMORAK) {
      return false;
    }

    return Static.getClient().getTickCount() - last >= 5
        && !Inventory.isFull()
        && (wine = TileItems.getNearest(
        Predicates.ids(ItemID.WINE_OF_ZAMORAK, ItemID.WINE_OF_ZAMORAK_23489))) != null
        && Standard.TELEKINETIC_GRAB.canCast();
  }

  @Override
  public int execute() {
    Standard.TELEKINETIC_GRAB.castOn(wine);
    last = Static.getClient().getTickCount();
    return 1;
  }
}
