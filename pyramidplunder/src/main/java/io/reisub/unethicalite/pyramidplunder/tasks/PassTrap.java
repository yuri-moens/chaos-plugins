package io.reisub.unethicalite.pyramidplunder.tasks;

import io.reisub.unethicalite.pyramidplunder.PyramidPlunder;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;

public class PassTrap extends Task {

  @Inject
  private PyramidPlunder plugin;

  private static final String FAILED_MESSAGE = "You fail to deactivate the trap mechanism.";

  private boolean failed;

  @Override
  public String getStatus() {
    return "Passing trap";
  }

  @Override
  public boolean validate() {
    return PyramidPlunder.isInPyramidPlunder() && !PyramidPlunder.isPastTraps();
  }

  @Override
  public int execute() {
    failed = false;

    final TileObject spearTrap = TileObjects.getNearest(ObjectID.SPEARTRAP);
    if (spearTrap == null) {
      return 1;
    }

    spearTrap.interact(0);
    Time.sleepTicksUntil(() -> PyramidPlunder.isPastTraps() || failed, 15);
    return 1;
  }

  @Subscribe
  private void onChatMessage(ChatMessage event) {
    if (!plugin.isRunning()) {
      return;
    }

    final String message = event.getMessage();

    if (message.equals(FAILED_MESSAGE)) {
      failed = true;
    }
  }
}
