package io.reisub.unethicalite.herblore.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.herblore.Herblore;
import io.reisub.unethicalite.herblore.HerbloreTask;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;

public class MakeCoconutMilk extends Task {
  @Inject private Herblore plugin;

  @Override
  public String getStatus() {
    return "Making coconut milk";
  }

  @Override
  public boolean validate() {
    return plugin.getConfig().task() == HerbloreTask.MAKE_COCONUT_MILK
        && plugin.getCurrentActivity() == Activity.IDLE
        && Inventory.contains(ItemID.HAMMER, ItemID.IMCANDO_HAMMER)
        && Inventory.contains(ItemID.COCONUT)
        && Inventory.contains(ItemID.VIAL);
  }

  @Override
  public void execute() {
    plugin.setActivity(Activity.MAKING_COCONUT_MILK);

    final List<Item> coconuts = Inventory.getAll(ItemID.COCONUT);
    final Item hammer = Inventory.getFirst(ItemID.HAMMER, ItemID.IMCANDO_HAMMER);

    coconuts.forEach(hammer::useOn);
    Time.sleepTicksUntil(() -> !Inventory.contains(ItemID.COCONUT), 3);

    final List<Item> halfCoconuts = Inventory.getAll(ItemID.HALF_COCONUT);
    final List<Item> vials = Inventory.getAll(ItemID.VIAL);

    for (int i = 0; i < halfCoconuts.size(); i++) {
      if (i >= vials.size()) {
        break;
      }

      halfCoconuts.get(i).useOn(vials.get(i));
      Time.sleepTick();
    }
  }
}
