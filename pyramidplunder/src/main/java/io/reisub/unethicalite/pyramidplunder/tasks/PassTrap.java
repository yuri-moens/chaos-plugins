package io.reisub.unethicalite.pyramidplunder.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.TileObjects;
import io.reisub.unethicalite.pyramidplunder.PyramidPlunder;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.eventbus.Subscribe;

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
  public void execute() {
    failed = false;

    final TileObject spearTrap = TileObjects.getNearest(ObjectID.SPEARTRAP);
    if (spearTrap == null) {
      return;
    }

    spearTrap.interact(0);
    Time.sleepTicksUntil(() -> PyramidPlunder.isPastTraps() || failed, 15);
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
