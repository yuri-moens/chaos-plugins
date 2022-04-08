package io.reisub.unethicalite.farming.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.game.GameThread;
import dev.hoot.api.game.Vars;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.utils.MessageUtils;
import io.reisub.unethicalite.farming.Config;
import io.reisub.unethicalite.farming.Farming;
import io.reisub.unethicalite.farming.Location;
import io.reisub.unethicalite.farming.PatchImplementation;
import io.reisub.unethicalite.farming.PatchState;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.TileObject;
import net.runelite.client.plugins.timetracking.farming.CropState;
import net.runelite.client.plugins.timetracking.farming.Produce;

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

        if (patch == null) {
            return false;
        }

        int varbit = Vars.getBit(plugin.getCurrentLocation().getVarbit());
        PatchState patchState = PatchImplementation.HERB.forVarbitValue(varbit);

        return !plugin.getCurrentLocation().isDone()
                && patchState != null
                && patchState.getProduce() == Produce.WEEDS
                && patchState.getCropState() == CropState.GROWING;
    }

    @Override
    public void execute() {
        TileObject patch = TileObjects.getNearest(Predicates.ids(Constants.HERB_PATCH_IDS));
        if (patch == null) {
            return;
        }

        List<Item> seeds = Inventory.getAll(Predicates.ids(Constants.HERB_SEED_IDS));
        if (seeds == null || seeds.isEmpty()) {
            plugin.getCurrentLocation().setDone(true);
            MessageUtils.addMessage("No seeds found. Skipping location.");
            return;
        }

        List<Item> diseaseFreeSeeds = Inventory.getAll(Predicates.names(Utils.parseStringList(config.diseaseFreeSeeds())));

        Item seed = null;

        if (!diseaseFreeSeeds.isEmpty()) {
            if (plugin.getCurrentLocation() == Location.TROLL_STRONGHOLD || plugin.getCurrentLocation() == Location.WEISS) {
                seed = diseaseFreeSeeds.get(0);
            } else {
                for (Item s : seeds) {
                    if (!diseaseFreeSeeds.contains(s)) {
                         seed = s;
                         break;
                    }
                }

                if (seed == null) {
                    seed = seeds.get(0);
                }
            }
        } else {
            seed = seeds.get(0);
        }

        if (seed == null) {
            return;
        }

        final Item finalSeed = seed;

        Time.sleepTicksUntil(() -> !Players.getLocal().isAnimating(), 3);
        GameThread.invoke(() -> finalSeed.useOn(patch));

        if (!Time.sleepTicksUntil(() -> Vars.getBit(plugin.getCurrentLocation().getVarbit()) > 3, 20)) {
            return;
        }

        final Item compost = Inventory.getFirst(Predicates.ids(Constants.COMPOST_IDS));
        if (compost == null) {
            return;
        }

        GameThread.invoke(() -> compost.useOn(patch));
        plugin.getCurrentLocation().setDone(true);
        Time.sleepTicks(3);
    }
}
