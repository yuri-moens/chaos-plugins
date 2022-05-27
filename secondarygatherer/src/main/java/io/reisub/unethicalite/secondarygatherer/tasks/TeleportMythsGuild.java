package io.reisub.unethicalite.secondarygatherer.tasks;

import io.reisub.unethicalite.secondarygatherer.Config;
import io.reisub.unethicalite.secondarygatherer.Secondary;
import io.reisub.unethicalite.secondarygatherer.SecondaryGatherer;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Inventory;

public class TeleportMythsGuild extends Task {

  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Teleporting";
  }

  @Override
  public boolean validate() {
    return config.secondary() == Secondary.BLUE_DRAGON_SCALE
        && !Inventory.contains(ItemID.BLUE_DRAGON_SCALE)
        && Inventory.contains(ItemID.MYTHICAL_CAPE_22114)
        && !Utils.isInRegion(SecondaryGatherer.MYTHS_GUILD_REGION,
        SecondaryGatherer.MYTHS_GUILD_DUMGEON_REGION);
  }

  @Override
  public void execute() {
    Inventory.getFirst(ItemID.MYTHICAL_CAPE_22114).interact("Teleport");
    Time.sleepTicks(6);
  }
}
