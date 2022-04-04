package io.reisub.unethicalite.nmz.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.game.GameThread;
import io.reisub.unethicalite.nmz.Config;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PickUp extends Task {
    @Inject
    private Config config;

    private static final int NMZ_MAP_REGION = 9033;

    private TileObject powerUp;

    @Override
    public String getStatus() {
        return "Picking up power up";
    }

    @Override
    public boolean validate() {
        return Utils.isInMapRegion(NMZ_MAP_REGION)
                && (powerUp = TileObjects.getNearest(Predicates.ids(getIds()))) != null;
    }

    @Override
    public void execute() {
        GameThread.invoke(() -> powerUp.interact(0));
        Time.sleepTicksUntil(() -> TileObjects.getNearest(Predicates.ids(getIds())) == null, 15);

        powerUp = null;
    }

    private Collection<Integer> getIds() {
        Set<Integer> ids = new HashSet<>();

        if (config.powerSurge()) {
            ids.add(ObjectID.POWER_SURGE);
        }

        if (config.recurrentDamage()) {
            ids.add(ObjectID.RECURRENT_DAMAGE);
        }

        if (config.zapper()) {
            ids.add(ObjectID.ZAPPER_26256);
        }

        if (config.ultimateForce()) {
            ids.add(ObjectID.ULTIMATE_FORCE);
        }

        return ids;
    }
}
