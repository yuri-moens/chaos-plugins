package io.reisub.unethicalite.blastfurnace.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.Vars;
import dev.unethicalite.api.items.Equipment;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.widgets.Production;
import io.reisub.unethicalite.blastfurnace.BlastFurnace;
import io.reisub.unethicalite.blastfurnace.Config;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.enums.Metal;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;
import net.runelite.api.Varbits;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.StatChanged;
import net.runelite.client.eventbus.Subscribe;

public class TakeBars extends Task {
  @Inject
  private BlastFurnace plugin;

  @Inject
  private Config config;

  private boolean experienceReceived;

  @Override
  public String getStatus() {
    return "Taking bars";
  }

  @Override
  public boolean validate() {
    if (plugin.getCurrentActivity() != Activity.IDLE
        || (plugin.getPreviousActivity() != Activity.DEPOSITING && plugin.getPreviousActivity() != Activity.WITHDRAWING)) {
      return false;
    }

    if (config.metal() == Metal.GOLD && !experienceReceived) {
      return false;
    }

    TileObject barDispenser = TileObjects.getFirstAt(new WorldPoint(1940, 4963, 0), "Bar dispenser");

    return barDispenser.hasAction("Take")
        && isAllOreProcessed()
        && plugin.isExpectingBars()
        && !Inventory.contains(ItemID.IRON_ORE, ItemID.MITHRIL_ORE, ItemID.COAL)
        && !Inventory.contains(config.metal().getBarId());
  }

  @Override
  public void execute() {
    plugin.setActivity(Activity.WITHDRAWING);

    TileObject barDispenser = TileObjects.getFirstAt(new WorldPoint(1940, 4963, 0), "Bar dispenser");

    if (config.metal() == Metal.GOLD) {
      if (!Equipment.contains(ItemID.ICE_GLOVES)) {
        Item gloves = Inventory.getFirst(ItemID.ICE_GLOVES);
        if (gloves == null) {
          return;
        }

        gloves.interact("Wear");
        Time.sleepTick();
      }
    }

    barDispenser.interact("Take");

    if (Time.sleepTicksUntil(Production::isOpen, 10)) {
      Production.chooseOption(1);
      experienceReceived = false;
    }

    Time.sleepTicksUntil(() -> plugin.getCurrentActivity() == Activity.IDLE, 5);
  }

  @Subscribe
  private void onStatChanged(StatChanged event) {
    if (event.getSkill() == Skill.SMITHING) {
      experienceReceived = true;
    }
  }

  private boolean isAllOreProcessed() {
    return Vars.getBit(Varbits.BLAST_FURNACE_COPPER_ORE)
        + Vars.getBit(Varbits.BLAST_FURNACE_TIN_ORE)
        + Vars.getBit(Varbits.BLAST_FURNACE_IRON_ORE)
        + Vars.getBit(Varbits.BLAST_FURNACE_MITHRIL_ORE)
        + Vars.getBit(Varbits.BLAST_FURNACE_ADAMANTITE_ORE)
        + Vars.getBit(Varbits.BLAST_FURNACE_RUNITE_ORE)
        + Vars.getBit(Varbits.BLAST_FURNACE_SILVER_ORE)
        + Vars.getBit(Varbits.BLAST_FURNACE_GOLD_ORE) == 0;
  }
}
