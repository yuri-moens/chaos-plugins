package io.reisub.unethicalite.glassblower.tasks;

import io.reisub.unethicalite.glassblower.Config;
import io.reisub.unethicalite.glassblower.Glassblower;
import io.reisub.unethicalite.utils.api.ChaosBank;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.BankTask;
import java.time.Duration;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileItems;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Inventory;

public class HandleBank extends BankTask {
  @Inject private Glassblower plugin;

  @Inject private Config config;

  @Override
  public boolean validate() {
    return isLastBankDurationAgo(Duration.ofSeconds(2)) && !Inventory.contains(ItemID.MOLTEN_GLASS);
  }

  @Override
  public void execute() {
    plugin.setActivity(Activity.IDLE);

    if (Players.getLocal().getWorldLocation().getRegionID()
        == Glassblower.FOSSIL_ISLAND_UNDERWATER_REGION) {
      TileObject rope = TileObjects.getNearest("Anchor rope");
      if (rope == null) {
        return;
      }

      rope.interact("Climb");
      if (!Time.sleepTicksUntil(
          () ->
              Players.getLocal().getWorldLocation().getRegionID()
                      == Glassblower.FOSSIL_ISLAND_SMALL_ISLAND_REGION
                  || TileItems.getNearest(ItemID.SEAWEED_SPORE) != null,
          30)) {
        return;
      }

      if (TileItems.getNearest(ItemID.SEAWEED_SPORE) != null) {
        return;
      }

      Time.sleepTick();
    }

    open();

    ChaosBank.depositAllExcept(false, ItemID.GLASSBLOWING_PIPE, ItemID.SEAWEED_SPORE);

    if (!Inventory.contains(ItemID.GLASSBLOWING_PIPE)) {
      Bank.withdraw(ItemID.GLASSBLOWING_PIPE, 1, Bank.WithdrawMode.ITEM);
    }

    if (config.pickUpSeaweedSpores() && !Inventory.contains(ItemID.SEAWEED_SPORE)) {
      Bank.withdraw(ItemID.SEAWEED_SPORE, 1, Bank.WithdrawMode.ITEM);
    }

    if (!Bank.contains(ItemID.MOLTEN_GLASS)) {
      plugin.stop("Out of materials. Stopping plugin.");
    }

    Bank.withdrawAll(ItemID.MOLTEN_GLASS, Bank.WithdrawMode.ITEM);
  }
}
