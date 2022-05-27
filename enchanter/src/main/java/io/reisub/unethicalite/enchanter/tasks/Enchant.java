package io.reisub.unethicalite.enchanter.tasks;

import io.reisub.unethicalite.enchanter.Config;
import io.reisub.unethicalite.enchanter.EnchantItem;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.magic.Magic;

public class Enchant extends Task {
  @Inject private Config config;

  @Override
  public String getStatus() {
    return "Enchanting";
  }

  @Override
  public boolean validate() {
    if (!config.spell().getSpell().canCast()) {
      return false;
    }

    if (config.item() == EnchantItem.ALL) {
      return Inventory.contains(Predicates.ids(EnchantItem.getAllItemsFor(config.spell())));
    } else {
      return Inventory.contains(config.item().getId());
    }
  }

  @Override
  public void execute() {
    List<Item> items;

    if (config.item() == EnchantItem.ALL) {
      items = Inventory.getAll(Predicates.ids(EnchantItem.getAllItemsFor(config.spell())));
    } else {
      items = Inventory.getAll(config.item().getId());
    }

    items.forEach(
        (i) -> {
          if (!config.spell().getSpell().canCast()) {
            return;
          }

          Magic.cast(config.spell().getSpell(), i);
          Time.sleepTicks(3);
        });
  }
}
