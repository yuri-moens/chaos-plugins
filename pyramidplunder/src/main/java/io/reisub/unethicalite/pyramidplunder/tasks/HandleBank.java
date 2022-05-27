package io.reisub.unethicalite.pyramidplunder.tasks;

import com.google.common.collect.ImmutableSet;
import io.reisub.unethicalite.pyramidplunder.PyramidPlunder;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ChaosBank;
import io.reisub.unethicalite.utils.tasks.BankTask;
import java.time.Duration;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Bank.WithdrawMode;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.widgets.Dialog;

public class HandleBank extends BankTask {

  @Inject
  private PyramidPlunder plugin;

  private static final Set<Integer> TREASURE_IDS = ImmutableSet.of(
      ItemID.IVORY_COMB,
      ItemID.GOLDEN_SCARAB, ItemID.STONE_SCARAB, ItemID.POTTERY_SCARAB,
      ItemID.GOLDEN_STATUETTE, ItemID.STONE_STATUETTE, ItemID.POTTERY_STATUETTE,
      ItemID.GOLD_SEAL, ItemID.STONE_SEAL
  );

  @Override
  public boolean validate() {
    return Utils.isInRegion(PyramidPlunder.SOPHANEM_BANK_REGION, Constants.CRAFTING_GUILD_REGION)
        && (Inventory.getFreeSlots() < 10 || hasTwoSceptres())
        && isLastBankDurationAgo(Duration.ofSeconds(10));
  }

  @Override
  public void execute() {
    if (!open(true)) {
      return;
    }

    ChaosBank.depositAll(false, Predicates.ids(TREASURE_IDS));
    Time.sleepTick();

    if (!Inventory.contains(Predicates.ids(Constants.ANTI_POISON_POTION_IDS))) {
      Bank.withdraw(Predicates.ids(ItemID.SUPERANTIPOISON4, ItemID.ANTIDOTE4), 1,
          WithdrawMode.ITEM);
    }

    if (Movement.getRunEnergy() < 80
        && !Inventory.contains(Predicates.ids(Constants.STAMINA_POTION_IDS))) {
      Bank.withdraw(ItemID.STAMINA_POTION4, 1, WithdrawMode.ITEM);
    }

    if (hasTwoSceptres()) {
      Bank.deposit(Predicates.ids(Constants.PHARAOHS_SCEPTRE_IDS), 1);
    }

    if (plugin.getSceptreCharges() == 1) {
      withdrawTreasure();
      Dialog.close();
    }

    final int amountOfFood = Math.floorDiv(Combat.getMissingHealth(), 12);

    for (int i = 0; i < amountOfFood; i++) {
      Bank.withdraw(ItemID.LOBSTER, 1, WithdrawMode.ITEM);
    }
  }

  private void withdrawTreasure() {
    int id = 0;
    int amount = 0;

    if (Bank.getCount(true, ItemID.STONE_STATUETTE) >= 12) {
      id = ItemID.STONE_STATUETTE;
      amount = 12;
    } else if (Bank.getCount(true, ItemID.STONE_SCARAB) >= 12) {
      id = ItemID.STONE_SCARAB;
      amount = 12;
    } else if (Bank.getCount(true, ItemID.STONE_SEAL) >= 12) {
      id = ItemID.STONE_SEAL;
      amount = 12;
    } else if (Bank.getCount(true, ItemID.GOLDEN_SCARAB) >= 6) {
      id = ItemID.GOLDEN_SCARAB;
      amount = 6;
    } else if (Bank.getCount(true, ItemID.GOLD_SEAL) >= 6) {
      id = ItemID.GOLD_SEAL;
      amount = 6;
    } else if (Bank.getCount(true, ItemID.GOLDEN_STATUETTE) >= 6) {
      id = ItemID.GOLDEN_STATUETTE;
      amount = 6;
    }

    if (id == 0) {
      plugin.stop("No treasure found to recharge sceptre with. Stopping plugin.");
      return;
    }

    final int finalId = id;
    final int finalAmount = amount;

    Bank.withdraw(finalId, finalAmount, WithdrawMode.ITEM);
    Time.sleepTicksUntil(() -> Inventory.getCount(finalId) >= finalAmount, 5);
  }

  private boolean hasTwoSceptres() {
    if (Inventory.getCount(Predicates.ids(Constants.PHARAOHS_SCEPTRE_IDS)) > 1) {
      return true;
    }

    return Inventory.contains(Predicates.ids(Constants.PHARAOHS_SCEPTRE_IDS))
        && Equipment.contains(Predicates.ids(Constants.PHARAOHS_SCEPTRE_IDS));
  }
}
