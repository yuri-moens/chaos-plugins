package io.reisub.unethicalite.daeyaltessence.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileObjects;
import io.reisub.unethicalite.daeyaltessence.DaeyaltEssence;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;

@RequiredArgsConstructor
public class Mine extends Task {
    private final DaeyaltEssence plugin;

    @Override
    public String getStatus() {
        return "Mining";
    }

    @Override
    public boolean validate() {
        return plugin.getCurrentActivity() != Activity.MINING
                && Players.getLocal().getWorldLocation().getRegionID() == DaeyaltEssence.ESSENCE_MINE_REGION;
    }

    @Override
    public void execute() {
        TileObject rock = TileObjects.getNearest(ObjectID.DAEYALT_ESSENCE_39095);
        if (rock == null) {
            return;
        }

        rock.interact("Mine");
        Time.sleepTicksUntil(() -> plugin.getCurrentActivity() == Activity.MINING, 20);
    }
}
