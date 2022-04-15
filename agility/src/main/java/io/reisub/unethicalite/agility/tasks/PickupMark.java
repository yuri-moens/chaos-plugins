package io.reisub.unethicalite.agility.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileItems;
import io.reisub.unethicalite.agility.Config;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Skill;
import net.runelite.api.TileItem;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.StatChanged;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;

public class PickupMark extends Task {
    @Inject
    private Config config;

    private boolean failed;
    private TileItem mark;

    @Override
    public String getStatus() {
        return "Picking up mark";
    }

    @Override
    public boolean validate() {
        if (failed) {
            return false;
        }

        mark = TileItems.getNearest("Mark of grace");

        return mark != null
                && config.courseSelection().isReachable(mark);
    }

    @Override
    public void execute() {
        Time.sleepTicksUntil(() -> Players.getLocal().isIdle(), 10);

        mark.interact("Take");
        Time.sleepTicksUntil(() -> TileItems.getNearest("Mark of grace") == null, 15);
    }

    @Subscribe
    private void onHitsplatApplied(HitsplatApplied event) {
        if (event.getHitsplat() != null && event.getHitsplat().isMine()) {
            failed = true;
        }
    }

    @Subscribe
    private void onStatChanged(StatChanged event) {
        if (event.getSkill().equals(Skill.AGILITY)) {
            failed = false;
        }
    }
}
