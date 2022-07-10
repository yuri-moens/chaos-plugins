package io.reisub.unethicalite.birdhouse.tasks;

import io.reisub.unethicalite.birdhouse.BirdHouse;
import io.reisub.unethicalite.birdhouse.Config;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.BankTask;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Dialog;

public class StartRun extends BankTask {

  @Inject
  private BirdHouse plugin;

  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Starting bird house run";
  }

  @Override
  public boolean validate() {
    return plugin.isManuallyStarted();
  }

  @Override
  public void execute() {
    open(true);

    if (!Inventory.isEmpty()) {
      Bank.depositInventory();
      Time.sleepTick();
    }

    Bank.withdraw(
        i -> i.getId() == ItemID.IMCANDO_HAMMER || i.getId() == ItemID.HAMMER,
        1,
        Bank.WithdrawMode.ITEM);
    Bank.withdraw(ItemID.CHISEL, 1, Bank.WithdrawMode.ITEM);
    Bank.withdraw(Predicates.ids(Constants.DIGSITE_PENDANT_IDS), 1, Bank.WithdrawMode.ITEM);
    Bank.withdraw(config.logs().getId(), 4, Bank.WithdrawMode.ITEM);
    Bank.withdraw(getSeedId(), 40, Bank.WithdrawMode.ITEM);

    switch (config.tpLocation()) {
      case EDGEVILLE:
      case FARMING_GUILD:
      case FEROX_ENCLAVE_DUELING_RING:
        Bank.withdraw(
            Predicates.ids(config.tpLocation().getTeleportItemIds()), 1, Bank.WithdrawMode.ITEM);
        break;
      default:
    }

    if (Bank.contains(Predicates.ids(Constants.GRACEFUL_GLOVES))) {
      Bank.withdraw(Predicates.ids(Constants.GRACEFUL_CAPE), 1, Bank.WithdrawMode.ITEM);
      Bank.withdraw(Predicates.ids(Constants.GRACEFUL_BOOTS), 1, Bank.WithdrawMode.ITEM);
      Bank.withdraw(Predicates.ids(Constants.GRACEFUL_GLOVES), 1, Bank.WithdrawMode.ITEM);
      Bank.withdraw(Predicates.ids(Constants.GRACEFUL_HOOD), 1, Bank.WithdrawMode.ITEM);
      Bank.withdraw(Predicates.ids(Constants.GRACEFUL_TOP), 1, Bank.WithdrawMode.ITEM);
      Bank.withdraw(Predicates.ids(Constants.GRACEFUL_LEGS), 1, Bank.WithdrawMode.ITEM);
    }

    close();
    Time.sleepTicksUntil(() -> !Bank.isOpen(), 5);

    Inventory.getAll(i -> i.getName().startsWith("Graceful")).forEach(i -> i.interact("Wear"));

    if (!Time.sleepTicksUntil(this::hasEverything, 3)) {
      return;
    }

    if (Dialog.isOpen()) {
      Dialog.close();
    }

    final Item pendant = Inventory.getFirst(Predicates.ids(Constants.DIGSITE_PENDANT_IDS));
    if (pendant == null) {
      return;
    }

    pendant.interact("Rub");
    Time.sleepTicksUntil(Dialog::isViewingOptions, 5);

    Dialog.chooseOption(2);

    plugin.setManuallyStarted(false);
  }

  public int getSeedId() {
    for (int id : Constants.BIRD_HOUSE_SEED_IDS) {
      if (Bank.getCount(true, id) >= 40) {
        return id;
      }
    }

    return 0;
  }

  private boolean hasEverything() {
    return (Inventory.contains(ItemID.IMCANDO_HAMMER, ItemID.HAMMER) || Equipment.contains(
        ItemID.IMCANDO_HAMMER))
        && Inventory.contains(ItemID.CHISEL)
        && Inventory.contains(Predicates.ids(Constants.DIGSITE_PENDANT_IDS))
        && Inventory.getCount(config.logs().getId()) == 4
        && Inventory.getCount(true, Predicates.ids(Constants.BIRD_HOUSE_SEED_IDS)) == 40;
  }
}
