package io.reisub.unethicalite.herblore.tasks;

import io.reisub.unethicalite.herblore.Herblore;
import io.reisub.unethicalite.herblore.Potion;
import io.reisub.unethicalite.herblore.Secondary;
import io.reisub.unethicalite.utils.api.ChaosBank;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.BankTask;
import java.time.Duration;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Inventory;

public class HandleBank extends BankTask {
  @Inject private Herblore plugin;

  private int itemsWithdrawn;

  @Override
  public String getStatus() {
    return "Banking";
  }

  @Override
  public boolean validate() {
    return plugin.getCurrentActivity() == Activity.IDLE
        && isLastBankDurationAgo(Duration.ofSeconds(2));
  }

  @Override
  public void execute() {
    open(3);

    deposit();

    int quantity = plugin.getConfig().quantity();

    switch (plugin.getConfig().task()) {
      case TAR_HERBS:
        if (quantity > 0 && itemsWithdrawn >= quantity) {
          plugin.stop("Finished tarring herbs");
        }

        withdrawForTarring();
        break;
      case PROCESS_SECONDARIES:
        if (quantity > 0 && itemsWithdrawn >= quantity) {
          plugin.stop("Finished processing secondaries");
        }

        withdrawForProcessing();
        break;
      case MAKE_COCONUT_MILK:
        if (quantity > 0 && itemsWithdrawn >= quantity) {
          plugin.stop("Finished making coconut milk");
        }

        withdrawForCoconutMilk();
        break;
      case CLEAN_HERBS:
        if (quantity > 0 && itemsWithdrawn >= quantity) {
          plugin.stop("Finished cleaning herbs");
        }

        withdrawForCleaning();
        break;
      case MAKE_UNFINISHED:
        if (quantity > 0 && itemsWithdrawn >= quantity) {
          plugin.stop("Finished making unfinished potions");
        }

        withdrawForUnfinished();
        break;
      case MAKE_POTION:
        if (quantity > 0 && itemsWithdrawn >= quantity) {
          plugin.stop("Finished making potions");
        }

        withdrawForPotions();
        break;
      default:
    }

    close();
    Time.sleepTicksUntil(() -> !Bank.isOpen(), 5);
  }

  private void deposit() {
    switch (plugin.getConfig().task()) {
      case TAR_HERBS:
        ChaosBank.depositAllExcept(false, ItemID.PESTLE_AND_MORTAR, ItemID.SWAMP_TAR);
        break;
      case PROCESS_SECONDARIES:
        ChaosBank.depositAllExcept(false, ItemID.PESTLE_AND_MORTAR);
        break;
      case MAKE_COCONUT_MILK:
        ChaosBank.depositAllExcept(false, ItemID.HAMMER, ItemID.IMCANDO_HAMMER);
        break;
      case MAKE_POTION:
        Item secondary = Inventory.getFirst(plugin.getConfig().potion().getSecondaryId());
        if (secondary != null && secondary.isStackable()) {
          ChaosBank.depositAllExcept(false, plugin.getConfig().potion().getSecondaryId());
        } else {
          Bank.depositInventory();
        }
        break;
      default:
        Bank.depositInventory();
    }
  }

  private void withdrawForTarring() {
    if (Bank.Inventory.getFirst(ItemID.PESTLE_AND_MORTAR) == null) {
      if (!Bank.contains(ItemID.PESTLE_AND_MORTAR)) {
        plugin.stop("Out of materials: no pestle and mortar");
      }

      Bank.withdraw(ItemID.PESTLE_AND_MORTAR, 1, Bank.WithdrawMode.ITEM);
    }

    if (Bank.Inventory.getFirst(ItemID.SWAMP_TAR) == null
        || Bank.Inventory.getFirst(ItemID.SWAMP_TAR).getQuantity() < 15) {
      if (!Bank.contains(ItemID.SWAMP_TAR)) {
        plugin.stop("Out of materials: no more swamp tar");
      }

      Bank.withdrawAll(ItemID.SWAMP_TAR, Bank.WithdrawMode.ITEM);
    }

    boolean found = false;
    int leftToTar = plugin.getConfig().quantity() - itemsWithdrawn;

    for (int i : plugin.getCleanTarHerbIds()) {
      int bankCount = Bank.getCount(true, i);
      if (bankCount > 0) {
        if (plugin.getConfig().quantity() > 0 && leftToTar < 26) {
          Bank.withdraw(i, leftToTar, Bank.WithdrawMode.ITEM);
          itemsWithdrawn += Math.min(bankCount, leftToTar);
        } else {
          Bank.withdrawAll(i, Bank.WithdrawMode.ITEM);
          itemsWithdrawn += Math.min(bankCount, 26);
        }
        found = true;
        break;
      }
    }

    if (!found) {
      for (int i : plugin.getGrimyTarHerbIds()) {
        int bankCount = Bank.getCount(true, i);
        if (bankCount > 0) {
          if (plugin.getConfig().quantity() > 0 && leftToTar < 26) {
            Bank.withdraw(i, leftToTar, Bank.WithdrawMode.ITEM);
            itemsWithdrawn += Math.min(bankCount, leftToTar);
          } else {
            Bank.withdrawAll(i, Bank.WithdrawMode.ITEM);
            itemsWithdrawn += Math.min(bankCount, 26);
          }
          found = true;
          break;
        }
      }
    }

    if (!found) {
      plugin.stop("Out of materials: no more herbs to tar");
    }
  }

  private void withdrawForProcessing() {
    if (Bank.Inventory.getFirst(ItemID.PESTLE_AND_MORTAR) == null) {
      if (!Bank.contains(ItemID.PESTLE_AND_MORTAR)) {
        plugin.stop("Out of materials: no pestle and mortar");
      }

      Bank.withdraw(ItemID.PESTLE_AND_MORTAR, 1, Bank.WithdrawMode.ITEM);
    }

    boolean found = false;
    int leftToProcess = plugin.getConfig().quantity() - itemsWithdrawn;

    for (int id : plugin.getBaseSecondaryIds()) {
      int bankCount = Bank.getCount(true, id);
      if (bankCount > 0) {
        if (id == Secondary.NIHIL_DUST.getOriginalId()
            || id == Secondary.CRYSTAL_DUST.getOriginalId()) {
          if (plugin.getConfig().quantity() > 0) {
            Bank.withdraw(id, plugin.getConfig().quantity(), Bank.WithdrawMode.ITEM);
            itemsWithdrawn += Math.min(bankCount, plugin.getConfig().quantity());
          } else {
            Bank.withdrawAll(id, Bank.WithdrawMode.ITEM);
            itemsWithdrawn += bankCount;
          }
        } else if (plugin.getConfig().quantity() > 0 && leftToProcess < 27) {
          Bank.withdraw(id, leftToProcess, Bank.WithdrawMode.ITEM);
          itemsWithdrawn += Math.min(bankCount, leftToProcess);
        } else {
          Bank.withdrawAll(id, Bank.WithdrawMode.ITEM);
          itemsWithdrawn += Math.min(bankCount, 27);
        }

        found = true;
        break;
      }
    }

    if (!found) {
      plugin.stop("Out of materials: no more secondaries to process");
    }
  }

  private void withdrawForCoconutMilk() {
    if (!Bank.contains(ItemID.VIAL)) {
      plugin.stop("Out of materials: no empty vials");
    }

    int bankCount = Bank.getCount(true, ItemID.HALF_COCONUT);

    if (bankCount > 0) {
      Bank.withdraw(ItemID.HALF_COCONUT, 14, Bank.WithdrawMode.ITEM);
      Bank.withdraw(ItemID.VIAL, 14, Bank.WithdrawMode.ITEM);

      itemsWithdrawn += Math.min(bankCount, 14);
    } else {
      if (Bank.Inventory.getFirst(ItemID.HAMMER, ItemID.IMCANDO_HAMMER) == null) {
        if (Bank.contains(ItemID.HAMMER)) {
          Bank.withdraw(ItemID.HAMMER, 1, Bank.WithdrawMode.ITEM);
        } else if (Bank.contains(ItemID.IMCANDO_HAMMER)) {
          Bank.withdraw(ItemID.IMCANDO_HAMMER, 1, Bank.WithdrawMode.ITEM);
        } else {
          plugin.stop("Out of materials: no hammer");
        }
      }

      bankCount = Bank.getCount(true, ItemID.COCONUT);

      if (bankCount == 0) {
        plugin.stop("Out of materials: no coconuts");
      }

      Bank.withdraw(ItemID.COCONUT, 13, Bank.WithdrawMode.ITEM);
      Bank.withdraw(ItemID.VIAL, 13, Bank.WithdrawMode.ITEM);

      itemsWithdrawn += Math.min(bankCount, 13);
    }
  }

  private boolean withdrawEither(int id1, int id2, int amount) {
    int count1 = Bank.getCount(true, id1);
    int count2 = Bank.getCount(true, id2);

    if (count1 == 0 && count2 == 0) {
      return false;
    }

    if (count1 > 0) {
      Bank.withdraw(id1, amount, Bank.WithdrawMode.ITEM);

      if (count1 >= amount) {
        return true;
      }
    }

    Bank.withdraw(id2, amount - count1, Bank.WithdrawMode.ITEM);

    return true;
  }

  private void withdrawForGuthixRestTea() {
    if (Bank.getCount(true, ItemID.CUP_OF_HOT_WATER) < 5) {
      plugin.stop("Out of materials: no cups of hot water");
    }

    Bank.withdraw(ItemID.CUP_OF_HOT_WATER, 5, Bank.WithdrawMode.ITEM);

    if (!withdrawEither(ItemID.HARRALANDER, ItemID.GRIMY_HARRALANDER, 5)) {
      plugin.stop("Out of materials: no more herbs");
    }

    if (!withdrawEither(ItemID.MARRENTILL, ItemID.GRIMY_MARRENTILL, 5)) {
      plugin.stop("Out of materials: no more herbs");
    }

    if (!withdrawEither(ItemID.GUAM_LEAF, ItemID.GRIMY_GUAM_LEAF, 10)) {
      plugin.stop("Out of materials: no more herbs");
    }

    itemsWithdrawn += 5;
  }

  private void withdrawForGuthixBalance() {
    int restoreCount = Bank.getCount(true, ItemID.RESTORE_POTION4);
    int unfCount = Bank.getCount(true, ItemID.GUTHIX_BALANCE_UNF);
    int garlicCount = Bank.getCount(true, ItemID.GARLIC);
    int dustCount = Bank.getCount(true, ItemID.SILVER_DUST);

    if ((restoreCount == 0 && unfCount == 0)
        || (unfCount == 0 && garlicCount == 0)
        || dustCount == 0) {
      plugin.stop("Out of materials for Guthix balance potions");
    }

    if (unfCount > 0) {
      Bank.withdraw(Potion.GUTHIX_BALANCE.getUnfinishedId(), 14, Bank.WithdrawMode.ITEM);
      Bank.withdraw(ItemID.SILVER_DUST, 14, Bank.WithdrawMode.ITEM);
      itemsWithdrawn += Math.min(unfCount, dustCount);
    } else {
      Bank.withdraw(Potion.GUTHIX_BALANCE.getBaseId(), 9, Bank.WithdrawMode.ITEM);
      Bank.withdraw(ItemID.GARLIC, 9, Bank.WithdrawMode.ITEM);
      Bank.withdraw(ItemID.SILVER_DUST, 9, Bank.WithdrawMode.ITEM);
      itemsWithdrawn += Math.min(Math.min(restoreCount, garlicCount), dustCount);
    }
  }

  private void withdrawForSanfewSerum() {
    int restoreCount = Bank.getCount(true, ItemID.SUPER_RESTORE4);
    int dustCount = Bank.getCount(true, ItemID.UNICORN_HORN_DUST);
    int grimyWeedCount = Bank.getCount(true, ItemID.GRIMY_SNAKE_WEED);
    int weedCount = Bank.getCount(true, ItemID.SNAKE_WEED);
    int nailCount = Bank.getCount(true, ItemID.NAIL_BEAST_NAILS);

    if (restoreCount == 0
        || dustCount == 0
        || (grimyWeedCount == 0 && weedCount == 0)
        || nailCount == 0) {
      plugin.stop("Out of materials for Sanfew serums");
    }

    Bank.withdraw(ItemID.SUPER_RESTORE4, 7, Bank.WithdrawMode.ITEM);
    Bank.withdraw(ItemID.UNICORN_HORN_DUST, 7, Bank.WithdrawMode.ITEM);
    Bank.withdraw(ItemID.NAIL_BEAST_NAILS, 7, Bank.WithdrawMode.ITEM);
    withdrawEither(ItemID.SNAKE_WEED, ItemID.GRIMY_SNAKE_WEED, 7);

    itemsWithdrawn +=
        Math.min(
            Math.min(Math.min(restoreCount, dustCount), (grimyWeedCount + weedCount)), nailCount);
  }

  private void withdrawForSuperCombatPotion() {
    int attackCount = Bank.getCount(true, ItemID.SUPER_ATTACK4);
    int strengthCount = Bank.getCount(true, ItemID.SUPER_STRENGTH4);
    int defenceCount = Bank.getCount(true, ItemID.SUPER_DEFENCE4);
    int grimyTorstolCount = Bank.getCount(true, ItemID.GRIMY_TORSTOL);
    int torstolCount = Bank.getCount(true, ItemID.TORSTOL);

    if (attackCount == 0
        || strengthCount == 0
        || (grimyTorstolCount == 0 && torstolCount == 0)
        || defenceCount == 0) {
      plugin.stop("Out of materials for Sanfew serums");
    }

    Bank.withdraw(ItemID.SUPER_ATTACK4, 7, Bank.WithdrawMode.ITEM);
    Bank.withdraw(ItemID.SUPER_STRENGTH4, 7, Bank.WithdrawMode.ITEM);
    Bank.withdraw(ItemID.SUPER_DEFENCE4, 7, Bank.WithdrawMode.ITEM);
    withdrawEither(ItemID.TORSTOL, ItemID.GRIMY_TORSTOL, 7);

    itemsWithdrawn +=
        Math.min(
            Math.min(Math.min(attackCount, strengthCount), (grimyTorstolCount + torstolCount)),
            defenceCount);
  }

  private void withdrawForPotions() {
    Potion potion = plugin.getConfig().potion();

    boolean stackableSecondaries = false;

    // handle exception potions
    switch (potion) {
      case GUTHIX_REST_TEA:
        withdrawForGuthixRestTea();
        return;
      case GUTHIX_BALANCE:
        withdrawForGuthixBalance();
        return;
      case SANFEW_SERUM:
        withdrawForSanfewSerum();
        return;
      case SUPER_COMBAT_POTION:
        withdrawForSuperCombatPotion();
        return;
      case DIVINE_BASTION_POTION:
      case DIVINE_BATTLEMAGE_POTION:
      case DIVINE_MAGIC_POTION:
      case DIVINE_RANGING_POTION:
      case DIVINE_SUPER_ATTACK_POTION:
      case DIVINE_SUPER_COMBAT_POTION:
      case DIVINE_SUPER_DEFENCE_POTION:
      case DIVINE_SUPER_STRENGTH_POTION:
      case STAMINA_POTION:
      case ANTI_VENOM:
      case EXTENDED_SUPER_ANTIFIRE:
      case COMPOST_POTION:
      case ANCIENT_BREW:
        stackableSecondaries = true;
        break;
      default:
    }

    if (potion.getUnfinishedId() == -1 || Bank.contains(potion.getUnfinishedId())) {
      final int quantity = stackableSecondaries ? 27 : 14;

      int baseCount;
      int id;

      if (potion.getUnfinishedId() == -1) {
        baseCount = Bank.getCount(true, potion.getBaseId());
        id = potion.getBaseId();
      } else {
        baseCount = Bank.getCount(true, potion.getUnfinishedId());
        id = potion.getUnfinishedId();
      }

      int secondaryCount =
          potion == Potion.ANTI_VENOM_PLUS
              ? Bank.getCount(true, ItemID.TORSTOL, ItemID.GRIMY_TORSTOL)
              : Bank.getCount(true, potion.getSecondaryId());

      if (stackableSecondaries) {
        secondaryCount += Inventory.getCount(true, potion.getSecondaryId());
      }

      if (baseCount == 0) {
        plugin.stop("Out of materials: no more bases");
      }

      if (secondaryCount == 0) {
        plugin.stop("Out of materials: no more secondaries");
      }

      Bank.withdraw(id, quantity, Bank.WithdrawMode.ITEM);

      if (potion == Potion.ANTI_VENOM_PLUS) {
        withdrawEither(ItemID.TORSTOL, ItemID.GRIMY_TORSTOL, quantity);
      } else {
        if (stackableSecondaries) {
          Bank.withdrawAll(potion.getSecondaryId(), Bank.WithdrawMode.ITEM);
        } else {
          Bank.withdraw(potion.getSecondaryId(), quantity, Bank.WithdrawMode.ITEM);
        }
      }

      itemsWithdrawn += Math.min(baseCount, secondaryCount);
    } else {
      final int quantity = stackableSecondaries ? 13 : 9;
      final int baseCount = Bank.getCount(true, potion.getBaseId());
      final int grimyHerbCount = Bank.getCount(true, potion.getHerb().getGrimyId());
      final int herbCount = Bank.getCount(true, potion.getHerb().getCleanId());
      int secondaryCount = Bank.getCount(true, potion.getSecondaryId());

      if (stackableSecondaries) {
        secondaryCount += Inventory.getCount(true, potion.getSecondaryId());
      }

      if (baseCount == 0) {
        plugin.stop("Out of materials: no more bases");
      }

      if (grimyHerbCount + herbCount == 0) {
        plugin.stop("Out of materials: no more herbs");
      }

      if (secondaryCount == 0) {
        plugin.stop("Out of materials: no more secondaries");
      }

      Bank.withdraw(potion.getBaseId(), quantity, Bank.WithdrawMode.ITEM);
      withdrawEither(potion.getHerb().getCleanId(), potion.getHerb().getGrimyId(), quantity);

      if (stackableSecondaries) {
        Bank.withdrawAll(potion.getSecondaryId(), Bank.WithdrawMode.ITEM);
      } else {
        Bank.withdraw(potion.getSecondaryId(), quantity, Bank.WithdrawMode.ITEM);
      }

      itemsWithdrawn += Math.min(Math.min(baseCount, grimyHerbCount + herbCount), secondaryCount);
    }
  }

  private void withdrawForUnfinished() {
    if (!Bank.contains(plugin.getConfig().base().getId())) {
      plugin.stop("Out of materials: no more bases");
    }

    Bank.withdraw(plugin.getConfig().base().getId(), 14, Bank.WithdrawMode.ITEM);

    boolean found = false;

    int leftToMake = plugin.getConfig().quantity() - itemsWithdrawn;

    for (int i : plugin.getCleanHerbIds()) {
      int bankCount = Bank.getCount(true, i);
      if (bankCount > 0) {
        if (plugin.getConfig().quantity() > 0 && leftToMake < 14) {
          Bank.withdraw(i, leftToMake, Bank.WithdrawMode.ITEM);
          itemsWithdrawn += Math.min(bankCount, leftToMake);
        } else {
          Bank.withdrawAll(i, Bank.WithdrawMode.ITEM);
          itemsWithdrawn += Math.min(bankCount, 14);
        }
        found = true;
        break;
      }
    }

    if (!found) {
      for (int i : plugin.getGrimyHerbIds()) {
        int bankCount = Bank.getCount(true, i);
        if (bankCount > 0) {
          if (plugin.getConfig().quantity() > 0 && leftToMake < 14) {
            Bank.withdraw(i, leftToMake, Bank.WithdrawMode.ITEM);
            itemsWithdrawn += Math.min(bankCount, leftToMake);
          } else {
            Bank.withdrawAll(i, Bank.WithdrawMode.ITEM);
            itemsWithdrawn += Math.min(bankCount, 14);
          }
          found = true;
          break;
        }
      }
    }

    if (!found) {
      plugin.stop("Out of materials: no more herbs to make unfinished potions with");
    }
  }

  private void withdrawForCleaning() {
    boolean found = false;

    int leftToClean = plugin.getConfig().quantity() - itemsWithdrawn;

    for (int i : plugin.getGrimyHerbIds()) {
      int bankCount = Bank.getCount(true, i);

      if (bankCount > 0) {
        if (plugin.getConfig().quantity() > 0 && leftToClean < 28) {
          Bank.withdraw(i, leftToClean, Bank.WithdrawMode.ITEM);
          itemsWithdrawn += Math.min(bankCount, leftToClean);
        } else {
          Bank.withdrawAll(i, Bank.WithdrawMode.ITEM);
          itemsWithdrawn += Math.min(bankCount, 28);
        }

        found = true;
        break;
      }
    }

    if (!found) {
      plugin.stop("Out of materials: no more herbs to clean");
    }
  }
}
