package io.reisub.unethicalite.glassblower.tasks;

import io.reisub.unethicalite.glassblower.Glassblower;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileItems;
import net.unethicalite.api.items.Inventory;

public class PickupSeed extends Task {
  @Inject private Glassblower plugin;

  @Override
  public String getStatus() {
    return "Picking up seed";
  }

  @Override
  public boolean validate() {
    return Players.getLocal().getWorldLocation().getRegionID()
            == Glassblower.FOSSIL_ISLAND_UNDERWATER_REGION
        && TileItems.getNearest(ItemID.SEAWEED_SPORE) != null;
  }

  @Override
  public void execute() {
    plugin.setActivity(Activity.IDLE);

    int quantity = Inventory.getCount(true, ItemID.SEAWEED_SPORE);

    TileItems.getNearest(ItemID.SEAWEED_SPORE).interact("Take");

    Time.sleepTicksUntil(
        () ->
            Inventory.getCount(true, ItemID.SEAWEED_SPORE) > quantity
                || Players.getLocal().getWorldLocation().getRegionID()
                    == Glassblower.FOSSIL_ISLAND_SMALL_ISLAND_REGION,
        30);
  }
}
