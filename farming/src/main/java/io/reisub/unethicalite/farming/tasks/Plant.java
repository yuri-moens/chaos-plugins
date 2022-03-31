package io.reisub.unethicalite.farming.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.game.Vars;
import dev.hoot.api.items.Inventory;
import io.reisub.unethicalite.farming.Config;
import io.reisub.unethicalite.farming.Farming;
import io.reisub.unethicalite.farming.Location;
import io.reisub.unethicalite.farming.SeedsMode;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.TileObject;

import javax.inject.Inject;
import java.util.List;

public class Plant extends Task {
    @Inject
    private Farming plugin;

    @Inject
    private Config config;

    @Override
    public String getStatus() {
        return "Planting seeds";
    }

    @Override
    public boolean validate() {
        TileObject patch = TileObjects.getNearest(Predicates.ids(Constants.HERB_PATCH_IDS));

        return patch != null && Vars.getBit(plugin.getCurrentLocation().getVarbit()) <= 3;
    }

    @Override
    public void execute() {
        TileObject patch = TileObjects.getNearest(Predicates.ids(Constants.HERB_PATCH_IDS));
        if (patch == null) {
            return;
        }

        List<Item> seeds = Inventory.getAll(Predicates.ids(Constants.HERB_SEED_IDS));
        if (seeds == null || seeds.isEmpty()) {
            plugin.stop("No seeds to plant. Stopping plugin.");
            return;
        }

        Item seed = null;

        if (config.seedsMode() == SeedsMode.LOWEST_FIRST_HIGHEST_ON_DISEASE_FREE || config.seedsMode() == SeedsMode.LOWEST_FIRST_HIGHEST_ON_DISEASE_FREE_PER_TWO) {
            if (plugin.getCurrentLocation() == Location.TROLL_STRONGHOLD || plugin.getCurrentLocation() == Location.WEISS) {
                for (Item s : seeds) {
                    if (seed == null) {
                        seed = s;
                    } else if (s.getId() > seed.getId()) {
                        seed = s;
                    }
                }
            } else {
                for (Item s : seeds) {
                    if (seed == null) {
                        seed = s;
                    } else if (s.getId() < seed.getId()) {
                        seed = s;
                    }
                }
            }
        } else {
            seed = seeds.get(0);
        }

        if (seed == null) {
            return;
        }

        Time.sleepTicksUntil(() -> !Players.getLocal().isAnimating(), 3);
        seed.useOn(patch);

        if (!Time.sleepTicksUntil(() -> Vars.getBit(plugin.getCurrentLocation().getVarbit()) > 3, 20)) {
            return;
        }

        Item compost = Inventory.getFirst(Predicates.ids(Constants.COMPOST_IDS));
        if (compost == null) {
            return;
        }

        compost.useOn(patch);
        plugin.getCurrentLocation().setDone(true);
        Time.sleepTicks(3);
    }
}
