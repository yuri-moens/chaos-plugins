package io.reisub.unethicalite.farming.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.items.Bank;
import dev.hoot.api.items.Inventory;
import io.reisub.unethicalite.farming.Config;
import io.reisub.unethicalite.farming.Farming;
import io.reisub.unethicalite.farming.Location;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.CBank;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.BankTask;
import net.runelite.api.Item;
import net.runelite.api.ItemID;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        open();

        CBank.depositAllExcept(
                ItemID.SEED_DIBBER,
                ItemID.SPADE,
                ItemID.MAGIC_SECATEURS,
                ItemID.BOTTOMLESS_COMPOST_BUCKET,
                ItemID.BOTTOMLESS_COMPOST_BUCKET_22997,
                ItemID.HERB_SACK,
                ItemID.OPEN_HERB_SACK
        );

        Bank.withdraw(dev.hoot.api.commons.Predicates.ids(ItemID.OPEN_HERB_SACK, ItemID.HERB_SACK), 1, Bank.WithdrawMode.ITEM);

        withdrawTeleportItems();
        withdrawSeeds();

        Time.sleepTicksUntil(() -> Inventory.contains(Predicates.ids(Constants.HERB_SEED_IDS)), 5);
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
                case HARMONY_ISLAND:
                case TROLL_STRONGHOLD:
                case WEISS:
                    runes.put(ItemID.AIR_RUNE, runes.get(ItemID.AIR_RUNE) + 1);
                    runes.put(ItemID.EARTH_RUNE, runes.get(ItemID.EARTH_RUNE) + 1);
                    runes.put(ItemID.LAW_RUNE, runes.get(ItemID.LAW_RUNE) + 1);
                    break;
                case PORT_PHASMATYS:
                    Bank.withdraw(ItemID.ECTOPHIAL, 1, Bank.WithdrawMode.ITEM);
                    break;
            }
        }

        for (Map.Entry<Integer, Integer> entry : runes.entrySet()) {
            if (entry.getValue() > 0) {
                Bank.withdraw(entry.getKey(), entry.getValue(), Bank.WithdrawMode.ITEM);
            }
        }
    }

    private void withdrawSeeds() {
        int seedsNeeded = plugin.getLocationQueue().size();
        int withdrawn = 0;

        List<Item> seeds = Bank.getAll(Predicates.ids(Constants.HERB_SEED_IDS));

        switch (config.seedsMode()) {
            case LOWEST_FIRST:
            case LOWEST_FIRST_HIGHEST_ON_DISEASE_FREE:
            case LOWEST_FIRST_PER_TWO:
            case LOWEST_FIRST_HIGHEST_ON_DISEASE_FREE_PER_TWO:
                seeds.sort(Comparator.comparingInt(Item::getId));
                break;
            case HIGHEST_FIRST:
            case HIGHEST_FIRST_PER_TWO:
                seeds.sort(Comparator.comparingInt(Item::getId).reversed());
                break;
        }

        switch (config.seedsMode()) {
            case LOWEST_FIRST:
            case HIGHEST_FIRST:
            case LOWEST_FIRST_HIGHEST_ON_DISEASE_FREE:
                for (Item seed : seeds) {
                    withdrawn += seed.getQuantity();

                    Bank.withdrawAll(seed.getId(), Bank.WithdrawMode.ITEM);

                    if (withdrawn >= seedsNeeded) {
                        break;
                    }
                }
                break;
            case LOWEST_FIRST_PER_TWO:
            case HIGHEST_FIRST_PER_TWO:
            case LOWEST_FIRST_HIGHEST_ON_DISEASE_FREE_PER_TWO:
                for (Item seed : seeds) {
                    if (seed.getQuantity() > 1) {
                        withdrawn += 2;
                    } else {
                        withdrawn += 1;
                    }

                    Bank.withdraw(seed.getId(), 2, Bank.WithdrawMode.ITEM);

                    if (withdrawn >= seedsNeeded) {
                        break;
                    }
                }
                break;
        }
    }
}
