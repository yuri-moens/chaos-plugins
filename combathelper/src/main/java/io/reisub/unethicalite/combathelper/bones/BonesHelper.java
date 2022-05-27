package io.reisub.unethicalite.combathelper.bones;

import io.reisub.unethicalite.combathelper.Helper;
import javax.inject.Singleton;
import net.runelite.api.Item;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.unethicalite.api.commons.Rand;
import net.unethicalite.api.items.Inventory;

@Singleton
public class BonesHelper extends Helper {
  private int[] itemIds;

  @Override
  public void startUp() {
    itemIds = parseItemIds();
  }

  @Override
  public void shutDown() {
    itemIds = null;
  }

  @Subscribe
  private void onConfigChanged(ConfigChanged event) {
    if (!event.getGroup().equals("chaoscombathelper")) {
      return;
    }

    if (event.getKey().equals("bones") || event.getKey().equals("ashes")) {
      itemIds = parseItemIds();
    }
  }

  @Subscribe
  private void onItemContainerChanged(ItemContainerChanged event) {
    if (config.bones().getId() == -1 && config.ashes().getId() == -1) {
      return;
    }

    plugin.schedule(
        () -> {
          Item buryItem = Inventory.getFirst(itemIds);
          if (buryItem != null) {
            buryItem.interact(1);
          }
        },
        Rand.nextInt(0, 100));
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
