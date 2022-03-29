package io.reisub.unethicalite.enchanter.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.magic.Magic;
import io.reisub.unethicalite.enchanter.Config;
import io.reisub.unethicalite.enchanter.EnchantItem;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;

import javax.inject.Inject;
import java.util.List;

public class Enchant extends Task {
    @Inject
    private Config config;

    @Override
    public String getStatus() {
        return "Enchanting";
    }

    @Override
    public boolean validate() {
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

        items.forEach((i) -> {
            Magic.cast(config.spell().getSpell(), i);
            Time.sleepTicks(3);
        });
    }
}
