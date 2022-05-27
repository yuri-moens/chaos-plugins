package io.reisub.unethicalite.birdhouse.tasks;

import io.reisub.unethicalite.birdhouse.BirdHouse;
import io.reisub.unethicalite.birdhouse.Config;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.BankTask;
import java.time.Duration;
import lombok.AllArgsConstructor;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Dialog;

@AllArgsConstructor
public class HandleBank extends BankTask {
  private final Config config;

  @Override
  public boolean validate() {
    return config.farmSeaweed()
        && isLastBankDurationAgo(Duration.ofSeconds(5))
        && Players.getLocal().distanceTo(BirdHouse.ISLAND) < 10
        && Inventory.contains((i) -> Constants.BIRD_NEST_IDS.contains(i.getId()));
  }

  @Override
  public void execute() {
    open(true);

    Bank.depositInventory();

    Bank.withdraw(ItemID.SEAWEED_SPORE, 1, Bank.WithdrawMode.ITEM);
    Bank.withdraw(ItemID.SEAWEED_SPORE, 1, Bank.WithdrawMode.ITEM);

    Bank.withdraw(ItemID.FISHBOWL_HELMET, 1, Bank.WithdrawMode.ITEM);
    Bank.withdraw(ItemID.DIVING_APPARATUS, 1, Bank.WithdrawMode.ITEM);
    Bank.withdraw(ItemID.FLIPPERS, 1, Bank.WithdrawMode.ITEM);

    close();
    Time.sleepTicksUntil(() -> !Bank.isOpen(), 5);

    Time.sleepTick();

    Inventory.getAll(ItemID.FISHBOWL_HELMET, ItemID.DIVING_APPARATUS, ItemID.FLIPPERS)
        .forEach((i) -> i.interact("Wear"));

    Item weapon = Equipment.fromSlot(EquipmentInventorySlot.WEAPON);
    Item offHand = Equipment.fromSlot(EquipmentInventorySlot.SHIELD);

    if (weapon != null) {
      weapon.interact("Remove");
    }

    if (offHand != null) {
      offHand.interact("Remove");
    }

    TileObject rowBoat = TileObjects.getNearest(ObjectID.ROWBOAT_30919);
    if (rowBoat == null) {
      return;
    }

    rowBoat.interact("Dive");
    Time.sleepTicksUntil(
        () ->
            Dialog.isViewingOptions()
                || Players.getLocal().getWorldLocation().getRegionID() == 15008,
        10);

    if (Dialog.isViewingOptions()) {
      Dialog.chooseOption(1);
      Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().getRegionID() == 15008, 10);
    }
  }
}
