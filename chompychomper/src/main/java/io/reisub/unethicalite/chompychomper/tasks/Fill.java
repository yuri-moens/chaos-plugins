package io.reisub.unethicalite.chompychomper.tasks;

import com.google.common.collect.ImmutableSet;
import io.reisub.unethicalite.chompychomper.ChompyChomper;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;

public class Fill extends Task {
  private static final Set<Integer> SWAMP_BUBBLES_IDS =
      ImmutableSet.of(
          ObjectID.SWAMP_BUBBLES,
          ObjectID.SWAMP_BUBBLES_735,
          ObjectID.SWAMP_BUBBLES_30667,
          ObjectID.SWAMP_BUBBLES_33640);
  @Inject private ChompyChomper plugin;

  @Override
  public String getStatus() {
    return "Filling bellows";
  }

  @Override
  public boolean validate() {
    return plugin.getCurrentActivity() == Activity.IDLE
        && Inventory.contains(ItemID.OGRE_BELLOWS)
        && !Inventory.contains(Predicates.ids(ChompyChomper.FILLED_BELLOW_IDS));
  }

  @Override
  public void execute() {
    TileObject bubbles = TileObjects.getNearest(Predicates.ids(SWAMP_BUBBLES_IDS));
    if (bubbles == null) {
      return;
    }

    List<Item> bellows = Inventory.getAll(ItemID.OGRE_BELLOWS);

    GameThread.invoke(() -> bellows.get(0).useOn(bubbles));
    Time.sleepTicksUntil(() -> Players.getLocal().isAnimating(), 20);
    Time.sleepTicks(1);

    bellows
        .subList(1, bellows.size())
        .forEach(
            (i) -> {
              GameThread.invoke(() -> i.useOn(bubbles));
              Time.sleepTicks(1);
            });
  }
}
