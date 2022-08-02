package io.reisub.unethicalite.herblore.tasks;

import io.reisub.unethicalite.herblore.Herblore;
import io.reisub.unethicalite.herblore.HerbloreTask;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Production;

public class MakePotion extends Task {

  @Inject
  private Herblore plugin;

  @Override
  public String getStatus() {
    return "Making potions";
  }

  @Override
  public boolean validate() {
    return plugin.getConfig().task() == HerbloreTask.MAKE_POTION
        && plugin.getCurrentActivity() == Activity.IDLE
        && Inventory.contains(plugin.getSecondaryIds())
        && (Inventory.contains(plugin.getBaseIds()) || hasSuperCombatIngredients());
  }

  @Override
  public int execute() {
    plugin.setActivity(Activity.CREATING_POTIONS);

    final List<Item> secondaries = Inventory.getAll(plugin.getSecondaryIds());
    final List<Item> bases = plugin.getBaseIds()[0] == -1 ? Inventory.getAll(ItemID.SUPER_ATTACK4)
        : Inventory.getAll(plugin.getBaseIds());

    if (secondaries.size() == 0 || bases.size() == 0) {
      return 1;
    }

    secondaries.get(0).useOn(bases.get(0));
    Time.sleepTicksUntil(Production::isOpen, 5);

    Production.chooseOption(1);

    return 1;
  }

  private boolean hasSuperCombatIngredients() {
    return Inventory.contains(ItemID.SUPER_ATTACK4)
        && Inventory.contains(ItemID.SUPER_STRENGTH4)
        && Inventory.contains(ItemID.SUPER_DEFENCE4);
  }
}
