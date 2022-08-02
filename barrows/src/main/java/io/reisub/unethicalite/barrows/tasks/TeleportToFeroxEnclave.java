package io.reisub.unethicalite.barrows.tasks;

import io.reisub.unethicalite.barrows.Barrows;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.Interact;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.api.commons.Time;

public class TeleportToFeroxEnclave extends Task {
  @Inject private Barrows plugin;

  private boolean finished = false;

  @Override
  public String getStatus() {
    return "Teleporting to Ferox Enclave";
  }

  @Override
  public boolean validate() {
    return finished;
  }

  @Override
  public int execute() {
    Interact.interactWithInventoryOrEquipment(
        Constants.DUELING_RING_IDS, "Rub", "Ferox Enclave", 3);

    if (Time.sleepTicksUntil(() -> Utils.isInRegion(Barrows.FEROX_ENCLAVE_REGIONS), 10)) {
      finished = false;
      plugin.reset();
    }

    return 1;
  }

  @Subscribe
  private void onWidgetLoaded(WidgetLoaded event) {
    if (event.getGroupId() == WidgetID.BARROWS_REWARD_GROUP_ID) {
      finished = true;
    }
  }
}
