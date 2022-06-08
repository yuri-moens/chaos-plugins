package io.reisub.unethicalite.sulliuscep.tasks;

import io.reisub.unethicalite.sulliuscep.Sulliuscep;
import io.reisub.unethicalite.sulliuscep.SulliuscepObject;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.items.Inventory;

public class GoToBank extends Task {

  @Inject
  private Sulliuscep plugin;

  @Override
  public String getStatus() {
    return "Going to bank";
  }

  @Override
  public boolean validate() {
    if (!Inventory.contains(Predicates.ids(Constants.BREW_POTION_IDS))
        && Combat.getCurrentHealth() < 20) {
      return true;
    }

    if (plugin.getCurrentSulliuscep() != SulliuscepObject.SULLIUSCEP_1
        || Utils.isInRegion(Sulliuscep.SWAMP_LOWER_REGION_ID)) {
      return false;
    }

    final int mushroomSlots = Inventory.getCount(ItemID.MUSHROOM)
        + Inventory.getFreeSlots()
        - Inventory.getCount(ItemID.MORT_MYRE_FUNGUS);

    return mushroomSlots < 9;
  }

  @Override
  public void execute() {
    final Item cape = Inventory.getFirst(Predicates.ids(Constants.CRAFTING_CAPE_IDS));

    if (cape == null) {
      return;
    }

    cape.interact("Teleport");
    Time.sleepTicksUntil(() -> Utils.isInRegion(Constants.CRAFTING_GUILD_REGION), 10);
    Time.sleepTicks(2);
  }
}
