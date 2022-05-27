package io.reisub.unethicalite.glassblower.tasks;

import io.reisub.unethicalite.glassblower.Config;
import io.reisub.unethicalite.glassblower.Glassblower;
import io.reisub.unethicalite.glassblower.Product;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileItems;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Dialog;
import net.unethicalite.api.widgets.Production;

public class Blow extends Task {
  @Inject private Glassblower plugin;

  @Inject private Config config;

  @Override
  public String getStatus() {
    return "Blowing";
  }

  @Override
  public boolean validate() {
    return plugin.getCurrentActivity() == Activity.IDLE
        && Inventory.contains(ItemID.GLASSBLOWING_PIPE)
        && Inventory.contains(ItemID.MOLTEN_GLASS);
  }

  @Override
  public void execute() {
    if (config.pickUpSeaweedSpores()
        && Players.getLocal().getWorldLocation().getRegionID()
            == Glassblower.FOSSIL_ISLAND_SMALL_ISLAND_REGION) {
      TileObject rowBoat = TileObjects.getNearest(ObjectID.ROWBOAT_30919);
      if (rowBoat == null) {
        return;
      }

      rowBoat.interact("Dive");
      Time.sleepTicksUntil(
          () ->
              Dialog.isViewingOptions()
                  || Players.getLocal().getWorldLocation().getRegionID()
                      == Glassblower.FOSSIL_ISLAND_UNDERWATER_REGION,
          10);

      if (Dialog.isViewingOptions()) {
        Dialog.chooseOption(1);
        Time.sleepTicksUntil(
            () ->
                Players.getLocal().getWorldLocation().getRegionID()
                    == Glassblower.FOSSIL_ISLAND_UNDERWATER_REGION,
            10);
      }

      Time.sleepTick();
    }

    if (config.pickUpSeaweedSpores() && TileItems.getNearest(ItemID.SEAWEED_SPORE) != null) {
      return;
    }

    Inventory.getFirst(ItemID.GLASSBLOWING_PIPE).useOn(Inventory.getFirst(ItemID.MOLTEN_GLASS));

    if (Time.sleepTicksUntil(Production::isOpen, 10)) {
      Product product =
          config.targetProduct() == Product.HIGHEST_POSSIBLE
              ? Product.getHighest()
              : config.targetProduct();

      if (product == null) {
        return;
      }

      Production.chooseOption(product.getOption());

      Time.sleepTicksUntil(() -> plugin.getCurrentActivity() == Activity.GLASSBLOWING, 5);
    }
  }
}
