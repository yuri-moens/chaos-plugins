package io.reisub.unethicalite.mahoganyhomes.tasks;

import io.reisub.unethicalite.mahoganyhomes.Config;
import io.reisub.unethicalite.mahoganyhomes.MahoganyHomes;
import io.reisub.unethicalite.mahoganyhomes.data.Home;
import io.reisub.unethicalite.mahoganyhomes.data.RequiredMaterials;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.tasks.BankTask;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Bank.WithdrawMode;
import net.unethicalite.api.items.Inventory;

public class HandleBank extends BankTask {

  @Inject
  private MahoganyHomes plugin;
  @Inject
  private Config config;

  private static final int MAX_STEEL_BARS = 3;

  @Override
  public boolean validate() {
    if (plugin.getCurrentHome() == null) {
      return false;
    }

    final RequiredMaterials requiredMaterials = plugin.getCurrentHome().getRequiredMaterials();

    final int planks = Inventory.getCount(config.plank().getPlankId())
        + plugin.getPlankSack().getPlankCount();
    final int steelBars = Inventory.getCount(ItemID.STEEL_BAR);

    return isLastBankDurationAgo(Duration.ofSeconds(10))
        && !plugin.getCurrentHome().isInHome(Players.getLocal())
        && (planks < requiredMaterials.getMaxPlanks()
        || steelBars < requiredMaterials.getMaxSteelBars()
        || (isPassingBank() && !Inventory.isFull()));
  }

  @Override
  public void execute() {
    plugin.teleport();

    if (Players.getLocal().getWorldLocation().getPlane() > 0) {
      plugin.useStairs();
    }

    final Home previousHome = plugin.getPreviousHome();
    final Home currentHome = plugin.getCurrentHome();

    WorldPoint nearest;

    if ((currentHome == Home.JESS || currentHome == Home.NOELLA)
        && previousHome == Home.ROSS) {
      nearest = Home.ROSS.getBankLocation();
    } else {
      nearest = currentHome.getBankLocation();
    }

    ChaosMovement.walkTo(nearest);

    if (!open(40, true)) {
      return;
    }

    final int requiredSteelBars = MAX_STEEL_BARS - Inventory.getCount(ItemID.STEEL_BAR);

    if (!Bank.contains(ItemID.STEEL_BAR) || !Bank.contains(config.plank().getPlankId())) {
      plugin.stop("Out of materials. Stopping plugin.");
      return;
    }

    for (int i = 0; i < requiredSteelBars; i++) {
      Bank.withdraw(ItemID.STEEL_BAR, 1, WithdrawMode.ITEM);
    }

    final Item plankSack = Bank.Inventory.getFirst(ItemID.PLANK_SACK);

    if (plankSack == null || plugin.getPlankSack().getPlankCount() == 28) {
      Bank.withdrawAll(config.plank().getPlankId(), WithdrawMode.ITEM);
    } else {
      do {
        Bank.withdrawAll(config.plank().getPlankId(), WithdrawMode.ITEM);
        Time.sleepTick();

        Item plankSack2 = Bank.Inventory.getFirst(ItemID.PLANK_SACK);
        plankSack2.interact("Use");

        Time.sleepTicksUntil(() -> Bank.Inventory.getAll().size() < 28
            || plugin.getPlankSack().getPlankCount() == 28, 3);

      } while (plugin.getPlankSack().getPlankCount() < 28);
    }

    last = Instant.now();
  }

  private boolean isPassingBank() {
    final Home currentHome = plugin.getCurrentHome();
    final Home previousHome = plugin.getPreviousHome();

    switch (currentHome) {
      case NOELLA:
        return previousHome == Home.ROSS;
      case LARRY:
      case NORMAN:
      case TAU:
        return !Utils.isInRegion(currentHome.getRegions());
      case BARBARA:
        return previousHome == Home.LEELA || previousHome == Home.MARIAH;
      case LEELA:
      case MARIAH:
        return !Utils.isInRegion(currentHome.getRegions()) || previousHome == Home.BARBARA;
      case SARAH:
        return previousHome == Home.BOB || previousHome == Home.JEFF;
      case BOB:
      case JEFF:
        return !Utils.isInRegion(currentHome.getRegions()) || previousHome == Home.SARAH;
      default:
        return false;
    }
  }
}
