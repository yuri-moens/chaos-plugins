package io.reisub.unethicalite.pyramidplunder.tasks;

import com.google.common.collect.ImmutableSet;
import io.reisub.unethicalite.pyramidplunder.PyramidPlunder;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.Varbits;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.Vars;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Reachable;

public class LootUrn extends Task {

  @Inject
  private PyramidPlunder plugin;

  private static final Set<Integer> URN_CLOSED_IDS = ImmutableSet.of(
      ObjectID.URN_21261,
      ObjectID.URN_21262,
      ObjectID.URN_21263
  );

  private static final String SUCCESS_MESSAGE = "You successfully loot the urn!";
  private static final String FAILED_MESSAGE = "You've been bitten by something moving inside the "
      + "urn.";

  private TileObject urn;
  private boolean finished;
  private boolean delay;

  @Override
  public String getStatus() {
    return "Looting urn";
  }

  @Override
  public boolean validate() {
    if (!PyramidPlunder.isInPyramidPlunder()) {
      return false;
    }

    urn = TileObjects.getNearest(o -> o.getName().equals("Urn") && o.hasAction("Check for Snakes"));

    if (urn == null || !Reachable.isInteractable(urn)) {
      return false;
    }

    final int ticksInMinigame = Vars.getBit(Varbits.PYRAMID_PLUNDER_TIMER);

    if (PyramidPlunder.isPenultimateRoom()) {
      return ticksInMinigame < 300;
    } else if (PyramidPlunder.isLastRoom()) {
      return ticksInMinigame < 475 || TileObjects.getNearest(
          o -> o.getName().equals("Grand Gold Chest") && o.hasAction("Search")) == null;
    } else {
      return false;
    }
  }

  @Override
  public void execute() {
    finished = false;
    delay = false;

    if (Inventory.isFull()) {
      final Item stoneItem = Inventory.getFirst(Predicates.nameContains("Stone"));
      if (stoneItem != null) {
        stoneItem.interact("Drop");
        Time.sleepTick();
      }
    }

    urn.interact(0);

    Time.sleepTicksUntil(() -> finished, 10);

    if (delay) {
      Time.sleepTicks(3);
    }
  }

  @Subscribe
  private void onChatMessage(ChatMessage event) {
    if (!plugin.isRunning()) {
      return;
    }

    final String message = event.getMessage();

    if (message.equals(FAILED_MESSAGE)) {
      finished = true;
    } else if (message.equals(SUCCESS_MESSAGE)) {
      delay = true;
      finished = true;
    }
  }
}
