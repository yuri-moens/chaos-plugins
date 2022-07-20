package io.reisub.unethicalite.farming.tasks;

import io.reisub.unethicalite.farming.Config;
import io.reisub.unethicalite.farming.Farming;
import io.reisub.unethicalite.farming.Location;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.ChaosBank;
import io.reisub.unethicalite.utils.api.ConfigList;
import io.reisub.unethicalite.utils.tasks.BankTask;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Dialog;

public class HandleBank extends BankTask {

  @Inject
  private Farming plugin;

  @Inject
  private Config config;

  @Override
  public boolean validate() {
    return !Inventory.contains(Predicates.ids(Constants.HERB_SEED_IDS))
        && !plugin.getLocationQueue().isEmpty();
  }

  @Override
  public void execute() {
    open(true);

    Time.sleepTicksUntil(Bank::isMainTabOpen, 3);

    ChaosBank.depositAllExcept(
        false,
        ItemID.SEED_DIBBER,
        ItemID.SPADE,
        ItemID.MAGIC_SECATEURS,
        ItemID.BOTTOMLESS_COMPOST_BUCKET,
        ItemID.BOTTOMLESS_COMPOST_BUCKET_22997,
        ItemID.HERB_SACK,
        ItemID.OPEN_HERB_SACK
    );

    Bank.withdraw(
        net.unethicalite.api.commons.Predicates.ids(ItemID.OPEN_HERB_SACK, ItemID.HERB_SACK),
        1,
        Bank.WithdrawMode.ITEM
    );

    withdrawTeleportItems();
    withdrawSeeds();

    Time.sleepTicksUntil(() -> Inventory.contains(Predicates.ids(Constants.HERB_SEED_IDS)), 5);
    Time.sleepTicks(2);

    if (Dialog.isOpen()) {
      Dialog.close();
    }
  }

  private void withdrawTeleportItems() {
    Map<Integer, Integer> runes = new HashMap<>();
    runes.put(ItemID.AIR_RUNE, 0);
    runes.put(ItemID.WATER_RUNE, 0);
    runes.put(ItemID.EARTH_RUNE, 0);
    runes.put(ItemID.LAW_RUNE, 0);

    for (Location location : Location.values()) {
      if (!location.isEnabled(config)) {
        continue;
      }

      switch (location) {
        case ARDOUGNE:
          if (config.useArdougneCloak()) {
            Bank.withdraw(Predicates.ids(Constants.ARDOUGNE_CLOAK_IDS), 1, Bank.WithdrawMode.ITEM);
          } else {
            runes.put(ItemID.WATER_RUNE, runes.get(ItemID.WATER_RUNE) + 2);
            runes.put(ItemID.LAW_RUNE, runes.get(ItemID.LAW_RUNE) + 2);
          }
          break;
        case CATHERBY:
          runes.put(ItemID.AIR_RUNE, runes.get(ItemID.AIR_RUNE) + 5);
          runes.put(ItemID.LAW_RUNE, runes.get(ItemID.LAW_RUNE) + 1);
          break;
        case FALADOR:
          if (config.useExplorersRing()) {
            Bank.withdraw(Predicates.ids(Constants.EXPLORERS_RING_IDS), 1, Bank.WithdrawMode.ITEM);
          } else {
            runes.put(ItemID.AIR_RUNE, runes.get(ItemID.AIR_RUNE) + 3);
            runes.put(ItemID.WATER_RUNE, runes.get(ItemID.WATER_RUNE) + 1);
            runes.put(ItemID.LAW_RUNE, runes.get(ItemID.LAW_RUNE) + 1);
          }
          break;
        case FARMING_GUILD:
          Bank.withdraw(Predicates.ids(Constants.SKILL_NECKLACE_IDS), 1, Bank.WithdrawMode.ITEM);
          break;
        case HOSIDIUS:
          if (config.useXericsTalisman()) {
            Bank.withdraw(ItemID.XERICS_TALISMAN, 1, Bank.WithdrawMode.ITEM);
            break;
          }
          // fall through
        case HARMONY_ISLAND:
          // fall through
        case TROLL_STRONGHOLD:
          // fall through
        case WEISS:
          runes.put(ItemID.AIR_RUNE, runes.get(ItemID.AIR_RUNE) + 1);
          runes.put(ItemID.EARTH_RUNE, runes.get(ItemID.EARTH_RUNE) + 1);
          runes.put(ItemID.LAW_RUNE, runes.get(ItemID.LAW_RUNE) + 1);
          break;
        case PORT_PHASMATYS:
          Bank.withdraw(ItemID.ECTOPHIAL, 1, Bank.WithdrawMode.ITEM);
          break;
        default:
      }
    }

    for (Map.Entry<Integer, Integer> entry : runes.entrySet()) {
      if (entry.getValue() > 0) {
        if (!Bank.contains(entry.getKey())) {
          Bank.withdraw(ItemID.RUNE_POUCH, 1, Bank.WithdrawMode.ITEM);
        } else {
          Bank.withdraw(entry.getKey(), entry.getValue(), Bank.WithdrawMode.ITEM);
        }
      }
    }

    if (Bank.contains(ItemID.CRAFTING_CAPE, ItemID.CRAFTING_CAPET)) {
      Bank.withdraw(
          Predicates.ids(ItemID.CRAFTING_CAPE, ItemID.CRAFTING_CAPET),
          1,
          Bank.WithdrawMode.ITEM
      );
    }
  }

  private void withdrawSeeds() {
    int quantityOfSeedsNeeded =
        plugin.getCurrentLocation() == null
            ? plugin.getLocationQueue().size()
            : plugin.getLocationQueue().size() + 1;
    int withdrawn = 0;
    ConfigList seedsToKeepList = ConfigList.parseList(config.seedsToKeep());

    List<Item> seeds;
    int wantedPerSeed = quantityOfSeedsNeeded;
    boolean hasWithdrawn;

    do {
      hasWithdrawn = false;

      if (config.manualMode()) {
        ConfigList manualSeedsList = ConfigList.parseList(config.manualSeeds());
        seeds = Bank.getAll(
            io.reisub.unethicalite.utils.api.Predicates.itemConfigList(manualSeedsList));

        wantedPerSeed =
            config.manualSeedsSplit()
                ? (quantityOfSeedsNeeded + seeds.size() - 1) / seeds.size()
                : quantityOfSeedsNeeded;
      } else {
        seeds = Bank.getAll(Predicates.ids(Constants.HERB_SEED_IDS));

        switch (config.seedsMode()) {
          case LOWEST_FIRST:
          case LOWEST_FIRST_PER_TWO:
            seeds.sort(Comparator.comparingInt(Item::getId));
            break;
          case HIGHEST_FIRST:
          case HIGHEST_FIRST_PER_TWO:
            seeds.sort(Comparator.comparingInt(Item::getId).reversed());
            break;
          default:
        }

        switch (config.seedsMode()) {
          case LOWEST_FIRST_PER_TWO:
          case HIGHEST_FIRST_PER_TWO:
            wantedPerSeed = 2;
            break;
          default:
        }
      }

      for (Item seed : seeds) {
        int quantity =
            seedsToKeepList.getStrings().containsKey(seed.getName())
                ? seed.getQuantity() - config.amountToKeep()
                : seed.getQuantity();

        if (quantity <= 0) {
          continue;
        }

        quantity = Math.min(quantity, wantedPerSeed);

        withdrawn += quantity;
        hasWithdrawn = true;

        if (quantity >= quantityOfSeedsNeeded) {
          Bank.withdrawAll(seed.getId(), Bank.WithdrawMode.ITEM);
        } else {
          Bank.withdraw(seed.getId(), quantity, Bank.WithdrawMode.ITEM);
        }

        if (withdrawn >= quantityOfSeedsNeeded) {
          break;
        }
      }

      if (hasWithdrawn && withdrawn < quantityOfSeedsNeeded) {
        Time.sleepTick();
        quantityOfSeedsNeeded -= withdrawn;
        withdrawn = 0;
      }
    } while (withdrawn == 0 && hasWithdrawn);

    if (config.limpwurt()) {
      Bank.withdrawAll(ItemID.LIMPWURT_SEED, Bank.WithdrawMode.ITEM);
    }
  }
}
