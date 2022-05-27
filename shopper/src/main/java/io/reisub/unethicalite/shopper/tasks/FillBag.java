package io.reisub.unethicalite.shopper.tasks;

import io.reisub.unethicalite.shopper.Shopper;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Inventory;

public class FillBag extends Task {
  @Inject private Shopper plugin;

  @Override
  public String getStatus() {
    return "Filling bag";
  }

  @Override
  public boolean validate() {
    return Inventory.contains(ItemID.COAL_BAG_12019)
        && Inventory.getCount(ItemID.COAL) > 5
        && plugin.getCoalInBag() < 26;
  }

  @Override
  public void execute() {
    int coalCount = Inventory.getCount(ItemID.COAL);
    plugin.setCoalInBag(plugin.getCoalInBag() + coalCount);

    Inventory.getFirst(ItemID.COAL_BAG_12019).interact("Fill");
    Time.sleepTicksUntil(() -> Inventory.getCount(ItemID.COAL) < coalCount, 3);
  }

  @Subscribe
  private void onChatMessage(ChatMessage event) {
    if (event.getMessage().equals("The coal bag is now empty.")) {
      plugin.setCoalInBag(0);
    }
  }
}
