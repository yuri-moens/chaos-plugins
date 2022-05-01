package io.reisub.unethicalite.plankmaker.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.magic.Magic;
import dev.unethicalite.api.magic.Regular;
import dev.unethicalite.managers.Static;
import io.reisub.unethicalite.plankmaker.Config;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;

public class TeleportCamelot extends Task {
  @Inject private Config config;

  @Override
  public String getStatus() {
    return "Teleporting to Camelot";
  }

  @Override
  public boolean validate() {
    return !Inventory.contains(config.logType().getLogId())
        && Regular.CAMELOT_TELEPORT.canCast()
        && Static.getClient().isInInstancedRegion();
  }

  @Override
  public void execute() {
    Magic.cast(Regular.CAMELOT_TELEPORT);
    Time.sleepTicksUntil(() -> !Static.getClient().isInInstancedRegion(), 5);
  }
}
