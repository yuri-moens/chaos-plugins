package io.reisub.unethicalite.combathelper.bones;

import dev.hoot.api.commons.Rand;
import dev.hoot.api.items.Inventory;
import io.reisub.unethicalite.combathelper.CombatHelper;
import io.reisub.unethicalite.combathelper.Config;
import net.runelite.api.Item;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.events.ConfigChanged;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BonesHelper {
    @Inject
    private CombatHelper plugin;

    @Inject
    private Config config;

    private int[] itemIds;

    public void startUp() {
        itemIds = parseItemIds();
    }

    public void shutDown() {
        itemIds =  null;
    }

    public void onConfigChanged(ConfigChanged event) {
        if (event.getKey().equals("bones") || event.getKey().equals("ashes")) {
            itemIds = parseItemIds();
        }
    }

    public void onItemContainerChanged(ItemContainerChanged event) {
        if (config.bones().getId() == -1 && config.ashes().getId() == -1) return;

        plugin.schedule(() -> {
            Item buryItem = Inventory.getFirst(itemIds);
            if (buryItem != null) {
                buryItem.interact(0);
            }
        }, Rand.nextInt(0, 100));
    }

    private int[] parseItemIds() {
        int[] ids;

        if (config.allBelow()) {
            Ashes[] allAshes = Ashes.allBelow(config.ashes());
            Bones[] allBones = Bones.allBelow(config.bones());

            ids = new int[allAshes.length + allBones.length];
            int i = 0;

            for (Ashes a : allAshes) {
                ids[i++] = a.getId();
            }

            for (Bones b : allBones) {
                ids[i++] = b.getId();
            }
        } else {
            ids = new int[2];

            ids[0] = config.ashes().getId();
            ids[1] = config.bones().getId();
        }

        return ids;
    }
}
