package io.reisub.unethicalite.blastfurnace.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.game.Vars;
import dev.unethicalite.api.items.Bank;
import dev.unethicalite.api.items.Bank.WithdrawMode;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.blastfurnace.BlastFurnace;
import io.reisub.unethicalite.blastfurnace.Config;
import io.reisub.unethicalite.utils.api.ChaosBank;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.enums.Metal;
import io.reisub.unethicalite.utils.tasks.BankTask;
import java.time.Duration;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;

public class HandleBank extends BankTask {
  @Inject private BlastFurnace plugin;

  @Inject private Config config;

  @Override
  public boolean validate() {
    return plugin.getCurrentActivity() == Activity.IDLE
        && (Inventory.contains(config.metal().getBarId())
            || (!plugin.isExpectingBars() && !Inventory.contains(ItemID.COAL)));
  }

  @Override
  public void execute() {
    plugin.setActivity(Activity.BANKING);
    plugin.setExpectingBars(false);

    open(15, 10);

    ChaosBank.depositAllExcept(
        false, ItemID.COAL_BAG_12019, ItemID.ICE_GLOVES, ItemID.GOLDSMITH_GAUNTLETS);
    Time.sleepTick();

    if (config.useStamina() && isStaminaExpiring(Duration.ofSeconds(10))) {
      drinkStamina();
    }

    withdraw(config.metal());

    plugin.setActivity(Activity.IDLE);
  }

  private void withdraw(Metal metal) {
    Item coalBag = Bank.Inventory.getFirst(ItemID.COAL_BAG_12019);

    switch (metal) {
      case STEEL:
        ChaosBank.bankInventoryInteract(coalBag, "Fill");
        Bank.withdrawAll(ItemID.IRON_ORE, Bank.WithdrawMode.ITEM);

        plugin.setExpectingBars(true);
        break;
      case GOLD:
        Bank.withdrawAll(ItemID.GOLD_ORE, Bank.WithdrawMode.ITEM);

        plugin.setExpectingBars(true);
        break;
      case MITHRIL:
        ChaosBank.bankInventoryInteract(coalBag, "Fill");

        if (getCoalInPot() == 0) {
          Bank.withdrawAll(ItemID.COAL, Bank.WithdrawMode.ITEM);
        } else {
          Bank.withdrawAll(ItemID.MITHRIL_ORE, Bank.WithdrawMode.ITEM);
          plugin.setExpectingBars(true);
        }
        break;
      case ADAMANTITE:
        ChaosBank.bankInventoryInteract(coalBag, "Fill");

        if (getCoalInPot() == 0) {
          Bank.withdrawAll(ItemID.COAL, Bank.WithdrawMode.ITEM);
        } else {
          Bank.withdrawAll(ItemID.ADAMANTITE_ORE, WithdrawMode.ITEM);
          plugin.setExpectingBars(true);
        }
        break;
      case RUNITE:
        final int coalInPot = getCoalInPot();

        if (coalInPot == 0 || coalInPot == 54 || coalInPot == 108) {
          Bank.withdrawAll(ItemID.COAL, Bank.WithdrawMode.ITEM);
        } else {
          Bank.withdrawAll(ItemID.RUNITE_ORE, WithdrawMode.ITEM);
          plugin.setExpectingBars(true);
        }
        break;
      default:
        plugin.stop("Unsupported metal type. Stopping plugin.");
        break;
    }
  }

  private int getCoalInPot() {
    return Vars.getBit(Varbits.BLAST_FURNACE_COAL);
  }
}
