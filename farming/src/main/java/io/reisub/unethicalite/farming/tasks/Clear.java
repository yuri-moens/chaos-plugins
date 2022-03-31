package io.reisub.unethicalite.farming.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.game.Vars;
import io.reisub.unethicalite.farming.Farming;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.TileObject;

import javax.inject.Inject;

public class Clear extends Task {
    @Inject
    private Farming plugin;

    @Override
    public String getStatus() {
        return "Clearing diseased herbs";
    }

    @Override
    public boolean validate() {
        TileObject patch = TileObjects.getNearest(Predicates.ids(Constants.HERB_PATCH_IDS));

        return patch != null && patch.hasAction("Clear");
    }

    @Override
    public void execute() {
        TileObject patch = TileObjects.getNearest(Predicates.ids(Constants.HERB_PATCH_IDS));
        if (patch == null) {
            return;
        }

        patch.interact("Clear");

        Time.sleepTicksUntil(() -> Vars.getBit(plugin.getCurrentLocation().getVarbit()) <= 3, 20);
    }
}
