package io.reisub.unethicalite.wintertodt.tasks;

import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import io.reisub.unethicalite.wintertodt.Config;
import io.reisub.unethicalite.wintertodt.Side;
import io.reisub.unethicalite.wintertodt.Wintertodt;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;
import net.runelite.api.NpcID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.movement.Movement;

public class ChangeSide extends Task {

  @Inject
  public Wintertodt plugin;
  @Inject
  public Config config;
  private Instant lastIncap;

  @Override
  public String getStatus() {
    return "Changing side";
  }

  @Override
  public boolean validate() {
    if (!plugin.bossIsUp()) {
      lastIncap = null;
      return false;
    }

    if (!NPCs.query()
        .distance(6)
        .ids(NpcID.INCAPACITATED_PYROMANCER)
        .results().isEmpty()
        && TileObjects.query()
        .distance(10)
        .names("Burning brazier")
        .results().isEmpty()) {
      if (lastIncap == null) {
        lastIncap = Instant.now();
      }
    } else {
      lastIncap = null;
      return false;
    }

    return lastIncap != null
        && Duration.between(lastIncap, Instant.now()).getSeconds() > config.sideTimeout()
        && plugin.getCurrentActivity() == Activity.IDLE;
  }

  @Override
  public void execute() {
    lastIncap = null;

    Movement.walk(Side.getFurthest().getPositionNearBrazier());
    Time.sleepTick();
  }
}
