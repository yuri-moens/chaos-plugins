package io.reisub.unethicalite.plankmaker.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.magic.Magic;
import dev.unethicalite.api.magic.Regular;
import dev.unethicalite.managers.Static;
import io.reisub.unethicalite.plankmaker.Config;
import io.reisub.unethicalite.plankmaker.PlankMaker;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;

public class TeleportHouse extends Task {
  @Inject private Config config;

  private static final int BUTLER_TASK_TIME = 12;

  @Override
  public String getStatus() {
    return "Teleporting to house";
  }

  @Override
  public boolean validate() {
    return Static.getClient().getTickCount() - PlankMaker.lastGive > BUTLER_TASK_TIME
        && Inventory.contains(config.logType().getLogId())
        && Inventory.contains("Coins")
        && Regular.TELEPORT_TO_HOUSE.canCast()
        && !Static.getClient().isInInstancedRegion();
  }

  @Override
  public void execute() {
    Magic.cast(Regular.TELEPORT_TO_HOUSE);
    Time.sleepTicksUntil(() -> Static.getClient().isInInstancedRegion(), 5);
  }
}
