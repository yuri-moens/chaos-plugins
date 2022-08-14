package io.reisub.unethicalite.barbarianassault.tasks;

import io.reisub.unethicalite.barbarianassault.BarbarianAssault;
import io.reisub.unethicalite.barbarianassault.data.Role;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.unethicalite.api.items.Inventory;

public class SpikeEgg extends Task {

  @Inject
  private BarbarianAssault plugin;

  @Override
  public String getStatus() {
    return "Spiking egg";
  }

  @Override
  public boolean validate() {
    return plugin.getRole() == Role.ATTACKER
        && Inventory.contains(ItemID.POISONED_EGG)
        && Inventory.contains(ItemID.SPIKES_10561);
  }

  @Override
  public void execute() {
    final Item egg = Inventory.getFirst(ItemID.POISONED_EGG);
    final Item spikes = Inventory.getFirst(ItemID.SPIKES_10561);

    if (egg == null || spikes == null) {
      return;
    }

    spikes.useOn(egg);
  }
}
