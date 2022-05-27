package io.reisub.unethicalite.pyramidplunder.tasks;

import com.google.common.collect.ImmutableSet;
import io.reisub.unethicalite.pyramidplunder.PyramidPlunder;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.TileObject;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;

public class OpenDoor extends Task {

  @Inject
  private PyramidPlunder plugin;

  private static final Set<String> FINISHED_MESSAGES = ImmutableSet.of(
      "This door leads to a dead end.",
      "You've already opened this door and it leads to a dead end.",
      "Your attempt fails."
  );

  private boolean finished;

  @Override
  public String getStatus() {
    return "Opening door";
  }

  @Override
  public boolean validate() {
    if (!PyramidPlunder.isInPyramidPlunder()) {
      return false;
    }

    return TileObjects.getNearest(
        o -> o.getName().equals("Grand Gold Chest") && o.hasAction("Search")) == null;
  }

  @Override
  public void execute() {
    finished = false;

    final String action = PyramidPlunder.isLastRoom() ? "Quick-leave" : "Pick-lock";

    final TileObject door = TileObjects.getNearest(
        o -> o.getName().equals("Tomb Door") && o.hasAction(action)
    );

    if (door == null) {
      return;
    }

    door.interact(action);

    Time.sleepTicksUntil(() -> finished || !PyramidPlunder.isPastTraps(), 40);
  }

  @Subscribe
  private void onChatMessage(ChatMessage event) {
    if (!plugin.isRunning()) {
      return;
    }

    if (FINISHED_MESSAGES.contains(event.getMessage())) {
      finished = true;
    }
  }
}
