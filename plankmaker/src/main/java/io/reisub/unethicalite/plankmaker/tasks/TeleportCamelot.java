package io.reisub.unethicalite.plankmaker.tasks;

import io.reisub.unethicalite.plankmaker.Config;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.magic.Magic;
import net.unethicalite.api.magic.SpellBook;
import net.unethicalite.client.Static;

public class TeleportCamelot extends Task {
  @Inject private Config config;

  @Override
  public String getStatus() {
    return "Teleporting to Camelot";
  }

  @Override
  public boolean validate() {
    return !Inventory.contains(config.logType().getLogId())
        && SpellBook.Standard.CAMELOT_TELEPORT.canCast()
        && Static.getClient().isInInstancedRegion();
  }

  @Override
  public void execute() {
    Magic.cast(SpellBook.Standard.CAMELOT_TELEPORT);
    Time.sleepTicksUntil(() -> !Static.getClient().isInInstancedRegion(), 5);
  }
}
