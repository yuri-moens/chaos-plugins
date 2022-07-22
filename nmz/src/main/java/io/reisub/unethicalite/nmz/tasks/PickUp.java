package io.reisub.unethicalite.nmz.tasks;

import io.reisub.unethicalite.nmz.Config;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;

public class PickUp extends Task {
  private static final int NMZ_MAP_REGION = 9033;
  @Inject private Config config;
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
