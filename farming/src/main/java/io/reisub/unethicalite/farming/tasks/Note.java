package io.reisub.unethicalite.farming.tasks;

import io.reisub.unethicalite.farming.Config;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;

public class Note extends Task {
  @Inject private Config config;

  @Override
  public String getStatus() {
    return "Noting products";
  }

  @Override
  public boolean validate() {
    return Inventory.contains(Predicates.ids(Constants.GRIMY_HERB_IDS))
        || Inventory.contains(Predicates.ids(Constants.CLEAN_HERB_IDS))
        || Inventory.contains(ItemID.LIMPWURT_ROOT);
  }

  @Override
  public int execute() {
    if (config.cleanHerbs() && Inventory.contains(Predicates.ids(Constants.GRIMY_HERB_IDS))) {
      Inventory.getAll(Predicates.ids(Constants.GRIMY_HERB_IDS)).forEach(i -> i.interact("Clean"));
      Time.sleepTicksUntil(() -> Inventory.contains(Predicates.ids(Constants.CLEAN_HERB_IDS)), 3);
    }

    Set<Integer> herbIds = new HashSet<>();
    herbIds.addAll(Constants.GRIMY_HERB_IDS);
    herbIds.addAll(Constants.CLEAN_HERB_IDS);

    final Item product =
        Inventory.contains(ItemID.LIMPWURT_ROOT)
            ? Inventory.getFirst(ItemID.LIMPWURT_ROOT)
            : Inventory.getFirst(Predicates.ids(herbIds));

    final NPC leprechaun = NPCs.getNearest("Tool Leprechaun");
    if (product == null || leprechaun == null) {
      return 1;
    }

    GameThread.invoke(() -> product.useOn(leprechaun));
    Time.sleepTicksUntil(() -> !Inventory.contains(product.getId()), 30);

    return 1;
  }
}
