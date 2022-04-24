package io.reisub.unethicalite.pickpocket.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.game.Skills;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.managers.Static;
import io.reisub.unethicalite.pickpocket.Config;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;

public class Eat extends Task {
  @Inject private Config config;

  private int last;

  @Override
  public String getStatus() {
    return "Eating";
  }

  @Override
  public boolean validate() {
    return (Skills.getBoostedLevel(Skill.HITPOINTS) <= config.eatHp()
            || config.healAtBank()
            || Inventory.isFull())
        && Inventory.contains(i -> i.hasAction("Eat", "Drink") && i.getId() != ItemID.BLOOD_PINT)
        && Static.getClient().getTickCount() > last + 3;
  }

  @Override
  public void execute() {
    Inventory.getFirst(i -> i.hasAction("Eat", "Drink") && i.getId() != ItemID.BLOOD_PINT)
        .interact(1);
    Time.sleepTick();

    last = Static.getClient().getTickCount();
  }
}
