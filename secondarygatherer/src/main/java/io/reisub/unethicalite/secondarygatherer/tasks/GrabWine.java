package io.reisub.unethicalite.secondarygatherer.tasks;

import dev.unethicalite.api.commons.Predicates;
import dev.unethicalite.api.entities.TileItems;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.magic.SpellBook.Standard;
import dev.unethicalite.client.Static;
import io.reisub.unethicalite.secondarygatherer.Config;
import io.reisub.unethicalite.secondarygatherer.Secondary;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.TileItem;

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
  public void execute() {
    Standard.TELEKINETIC_GRAB.castOn(wine);
    last = Static.getClient().getTickCount();
  }
}
