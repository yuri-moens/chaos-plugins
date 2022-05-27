package io.reisub.unethicalite.barrows.tasks;

import io.reisub.unethicalite.barrows.Barrows;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.game.Skills;

public class DrinkPool extends Task {
  private TileObject pool;

  @Override
  public String getStatus() {
    return "Drinking from pool";
  }

  @Override
  public boolean validate() {
    return Utils.isInRegion(Barrows.FEROX_ENCLAVE_REGIONS)
        && Skills.getBoostedLevel(Skill.PRAYER) < Skills.getLevel(Skill.PRAYER)
        && (pool = TileObjects.getNearest(Predicates.ids(Constants.REJUVENATION_POOL_IDS))) != null;
  }

  @Override
  public void execute() {
    GameThread.invoke(() -> pool.interact("Drink"));
    Time.sleepTicksUntil(
        () -> Skills.getBoostedLevel(Skill.PRAYER) == Skills.getLevel(Skill.PRAYER), 20);
    Time.sleepTicks(3);
  }
}
