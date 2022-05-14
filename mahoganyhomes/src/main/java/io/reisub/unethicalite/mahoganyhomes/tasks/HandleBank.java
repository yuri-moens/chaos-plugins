package io.reisub.unethicalite.mahoganyhomes.tasks;

import dev.unethicalite.api.commons.Rand;
import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.items.Bank;
import dev.unethicalite.api.items.Bank.WithdrawMode;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.movement.pathfinder.BankLocation;
import io.reisub.unethicalite.mahoganyhomes.Config;
import io.reisub.unethicalite.mahoganyhomes.Home;
import io.reisub.unethicalite.mahoganyhomes.MahoganyHomes;
import io.reisub.unethicalite.mahoganyhomes.RequiredMaterials;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ChaosBank;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.api.Interact;
import io.reisub.unethicalite.utils.tasks.BankTask;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.coords.WorldPoint;

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
    teleport();

    final Home previousHome = plugin.getPreviousHome();
    final Home currentHome = plugin.getCurrentHome();

    BankLocation nearest;

    if ((currentHome == Home.JESS || currentHome == Home.NOELLA)
        && previousHome == Home.ROSS) {
      nearest = BankLocation.ARDOUGNE_NORTH_BANK;
    } else {
      nearest = currentHome.getBankLocation();
    }

    final List<WorldPoint> bankAreaPoints = nearest.getArea().toWorldPointList();

    ChaosMovement.walkTo(bankAreaPoints.get(Rand.nextInt(0, bankAreaPoints.size())));

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

    while (Bank.Inventory.getAll().size() < 28) {
      Bank.withdrawAll(config.plank().getPlankId(), WithdrawMode.ITEM);

      final Item plankSack = Bank.Inventory.getFirst(ItemID.PLANK_SACK, ItemID.PLANK_SACK_25629);

      if (plankSack != null && plugin.getPlankSack().getPlankCount() != 28) {
        Time.sleepTick();
        ChaosBank.bankInventoryInteract(plankSack, "Use");
        Time.sleepTicksUntil(() -> Bank.Inventory.getAll().size() < 28
            || plugin.getPlankSack().getPlankCount() == 28, 3);
      }
    }

    last = Instant.now();
  }

  private void teleport() {
    final Home home = plugin.getCurrentHome();

    if (Utils.isInRegion(home.getRegions())) {
      return;
    }

    final Item tab = Inventory.getFirst(home.getTabletId());

    if (tab == null) {
      if (home.getTabletId() == ItemID.TELEPORT_TO_HOUSE) {
        Interact.interactWithInventoryOrEquipment(
            ItemID.XERICS_TALISMAN,
            "Rub",
            "Xeric's Glade",
            4
        );
      } else {
        plugin.stop("Couldn't find teleport tab. Stopping plugin.");
        return;
      }
    } else {
      tab.interact("Break");
    }

    Time.sleepTicksUntil(() -> Utils.isInRegion(home.getRegions()), 10);
    Time.sleepTicks(2);
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
