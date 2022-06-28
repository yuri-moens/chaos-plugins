package io.reisub.unethicalite.agility.tasks;

import io.reisub.unethicalite.agility.Agility;
import io.reisub.unethicalite.agility.Config;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;
import net.runelite.api.events.StatChanged;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Production;
import net.unethicalite.client.Static;

public class Fletch extends Task {

  @Inject
  private Config config;
  private int lastTick;
  private boolean ready;

  @Override
  public String getStatus() {
    return "Fletching";
  }

  @Override
  public boolean validate() {
    if (!config.fletch()) {
      return false;
    }

    final boolean hasFeathers = Inventory.contains(ItemID.FEATHER);
    final boolean hasShafts = Inventory.contains(ItemID.ARROW_SHAFT);
    final boolean hasHeads = Inventory.contains(ItemID.BROAD_ARROWHEADS);
    final boolean hasHeadless = Inventory.contains(ItemID.HEADLESS_ARROW);

    return ready
        && ((hasFeathers && hasShafts) || (hasHeads && hasHeadless));
  }

  @Override
  public void execute() {
    ready = false;

    if (Agility.DELAY_POINTS.contains(Players.getLocal().getWorldLocation())) {
      Time.sleepTick();
    }

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

    lastTick = Static.getClient().getTickCount();
  }

  @Subscribe
  private void onStatChanged(StatChanged event) {
    if (event.getSkill() == Skill.AGILITY) {
      ready = true;
    }
  }
}
