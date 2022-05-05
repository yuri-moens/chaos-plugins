package io.reisub.unethicalite.secondarygatherer.tasks;

import dev.unethicalite.api.commons.Predicates;
import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.secondarygatherer.Config;
import io.reisub.unethicalite.secondarygatherer.Secondary;
import io.reisub.unethicalite.secondarygatherer.SecondaryGatherer;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;

public class TeleportCraftingGuild extends Task {

  @Inject
  private SecondaryGatherer plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Teleporting";
  }

  @Override
  public boolean validate() {
    return config.secondary() == Secondary.BLUE_DRAGON_SCALE
        && Inventory.isFull()
        && Inventory.contains(Predicates.ids(ItemID.CRAFTING_CAPE, ItemID.CRAFTING_CAPET))
        && Utils.isInRegion(SecondaryGatherer.MYTHS_GUILD_DUMGEON_REGION);
  }

  @Override
  public void execute() {
    if (plugin.getCombatHelper().getPrayerHelper().isFlicking()) {
      plugin.getCombatHelper().getPrayerHelper().toggleFlicking();
      Time.sleepTick();
    }

    Inventory.getFirst(Predicates.ids(ItemID.CRAFTING_CAPE, ItemID.CRAFTING_CAPET))
        .interact("Teleport");
    Time.sleepTicks(6);
  }
}
