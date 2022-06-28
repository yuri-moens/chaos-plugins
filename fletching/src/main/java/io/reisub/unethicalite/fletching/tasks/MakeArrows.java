package io.reisub.unethicalite.fletching.tasks;

import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;
import net.runelite.api.events.StatChanged;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Production;
import net.unethicalite.client.Static;

public class MakeArrows extends Task {

  private int createCount;
  private int lastTick;

  @Override
  public String getStatus() {
    return "Making arrows";
  }

  @Override
  public boolean validate() {
    if (createCount % 10 != 0) {
      if (Static.getClient().getTickCount() - lastTick < 3) {
        return false;
      }
    }

    final boolean hasFeathers = Inventory.contains(ItemID.FEATHER);
    final boolean hasShafts = Inventory.contains(ItemID.ARROW_SHAFT);
    final boolean hasHeads = Inventory.contains(ItemID.BROAD_ARROWHEADS);
    final boolean hasHeadless = Inventory.contains(ItemID.HEADLESS_ARROW);

    return (hasFeathers && hasShafts) || (hasHeads && hasHeadless);
  }

  @Override
  public void execute() {
    final Item feathers = Inventory.getFirst(ItemID.FEATHER);
    final Item shafts = Inventory.getFirst(ItemID.ARROW_SHAFT);
    final Item heads = Inventory.getFirst(ItemID.BROAD_ARROWHEADS);
    final Item headless = Inventory.getFirst(ItemID.HEADLESS_ARROW);

    if (feathers != null && shafts != null) {
      feathers.useOn(shafts);
    } else if (headless != null && heads != null) {
      heads.useOn(headless);
    }

    if (!Time.sleepTicksUntil(Production::isOpen, 5)) {
      return;
    }

    Production.chooseOption(1);

    Time.sleepTicks(3);
  }

  @Subscribe
  private void onStatChanged(StatChanged event) {
    if (event.getSkill() == Skill.FLETCHING) {
      if (lastTick != Static.getClient().getTickCount()) {
        createCount++;
        lastTick = Static.getClient().getTickCount();
      }
    }
  }
}
