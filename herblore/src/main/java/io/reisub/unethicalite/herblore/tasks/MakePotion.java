package io.reisub.unethicalite.herblore.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.widgets.Production;
import io.reisub.unethicalite.herblore.Herblore;
import io.reisub.unethicalite.herblore.HerbloreTask;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;

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
  public void execute() {
    plugin.setActivity(Activity.CREATING_POTIONS);

    final List<Item> secondaries = Inventory.getAll(plugin.getSecondaryIds());
    final List<Item> bases = plugin.getBaseIds()[0] == -1 ? Inventory.getAll(ItemID.SUPER_ATTACK4)
        : Inventory.getAll(plugin.getBaseIds());

    if (secondaries.size() == 0 || bases.size() == 0) {
      return;
    }

    secondaries.get(0).useOn(bases.get(0));
    Time.sleepTicksUntil(Production::isOpen, 5);

    Production.chooseOption(1);
  }

  private boolean hasSuperCombatIngredients() {
    return Inventory.contains(ItemID.SUPER_ATTACK4)
        && Inventory.contains(ItemID.SUPER_STRENGTH4)
        && Inventory.contains(ItemID.SUPER_DEFENCE4);
  }
}
