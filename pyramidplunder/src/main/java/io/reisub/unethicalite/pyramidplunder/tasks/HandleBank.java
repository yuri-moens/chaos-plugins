package io.reisub.unethicalite.pyramidplunder.tasks;

import com.google.common.collect.ImmutableSet;
import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.game.Combat;
import dev.unethicalite.api.items.Bank;
import dev.unethicalite.api.items.Bank.WithdrawMode;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.pyramidplunder.PyramidPlunder;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ChaosBank;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.BankTask;
import java.time.Duration;
import java.util.Set;
import net.runelite.api.ItemID;

public class HandleBank extends BankTask {

  private static final Set<Integer> TREASURE_IDS = ImmutableSet.of(
      ItemID.IVORY_COMB,
      ItemID.GOLDEN_SCARAB, ItemID.STONE_SCARAB, ItemID.POTTERY_SCARAB,
      ItemID.GOLDEN_STATUETTE, ItemID.STONE_STATUETTE, ItemID.POTTERY_STATUETTE,
      ItemID.GOLD_SEAL, ItemID.STONE_SEAL
  );

  @Override
  public boolean validate() {
    return Utils.isInRegion(PyramidPlunder.SOPHANEM_BANK_REGION)
        && Inventory.getFreeSlots() < 14
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
      Bank.withdraw(ItemID.SUPERANTIPOISON4, 1, WithdrawMode.ITEM);
    }

    if (!Inventory.contains(Predicates.ids(Constants.STAMINA_POTION_IDS))) {
      Bank.withdraw(ItemID.STAMINA_POTION4, 1, WithdrawMode.ITEM);
    }

    final int amountOfFood = Math.floorDiv(Combat.getMissingHealth(), 12);

    for (int i = 0; i < amountOfFood; i++) {
      Bank.withdraw(ItemID.LOBSTER, 1, WithdrawMode.ITEM);
    }
  }
}
