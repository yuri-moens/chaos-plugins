package io.reisub.unethicalite.woodcutting.tasks;

import com.google.common.collect.Maps;
import io.reisub.unethicalite.utils.tasks.Task;
import io.reisub.unethicalite.woodcutting.Config;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Inject;
import net.runelite.api.TileItem;
import net.runelite.api.TileObject;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.ItemSpawned;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileItems;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.client.Static;

public class PickupNest extends Task {

  @Inject
  private Config config;
  @Inject
  private Chop chopTask;
  private int birdNestAppeared;
  private final Map<TileItem, Integer> nestMap = Maps.newHashMap();

  @Override
  public String getStatus() {
    return "Picking up bird's nest";
  }

  @Override
  public boolean validate() {
    return config.birdNests()
        && !Inventory.isFull()
        && getNestToPickUp() != null;
  }

  @Override
  public int execute() {
    final TileItem nest = getNestToPickUp();

    if (nest == null) {
      return 1;
    }

    GameThread.invoke(() -> nest.interact("Take"));

    Time.sleepTicksUntil(
        () -> TileItems.getFirstAt(
            nest.getWorldLocation(),
            Predicates.nameContains("nest", false)
        ) == null, 20);

    final TileObject tree = TileObjects.getFirstAt(
        chopTask.getCurrentTreePosition()
            .dx(config.location().getXoffset())
            .dy(config.location().getYoffset()),
        Predicates.ids(config.location().getTreeIds())
    );

    if (tree == null) {
      chopTask.setCurrentTreePosition(null);
      return 1;
    }

    GameThread.invoke(() -> tree.interact("Chop down"));
    return 2;
  }

  private TileItem getNestToPickUp() {
    if (config.onlyPickUpOurs()) {
      for (Entry<TileItem, Integer> entry : nestMap.entrySet()) {
        final int tickDelta = Math.abs(entry.getValue() - birdNestAppeared);

        if (tickDelta <= 2) {
          return entry.getKey();
        }
      }

      return null;
    } else {
      return TileItems.getNearest(Predicates.nameContains("nest", false));
    }
  }

  @Subscribe
  private void onItemSpawned(ItemSpawned event) {
    final TileItem item = event.getItem();

    if (item != null && item.getName().toLowerCase().contains("nest")) {
      nestMap.put(item, Static.getClient().getTickCount());
    }
  }

  @Subscribe
  private void onItemDespawned(ItemDespawned event) {
    nestMap.remove(event.getItem());
  }

  @Subscribe
  private void onChatMessage(ChatMessage event) {
    if (event.getMessage().contains("A bird's nest falls out of the tree")) {
      birdNestAppeared = Static.getClient().getTickCount();
    }
  }
}
