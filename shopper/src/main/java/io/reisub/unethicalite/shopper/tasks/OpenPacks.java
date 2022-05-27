package io.reisub.unethicalite.shopper.tasks;

import io.reisub.unethicalite.shopper.Config;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.items.Shop;
import net.unethicalite.api.packets.DialogPackets;
import net.unethicalite.client.Static;

public class OpenPacks extends Task {
  @Inject private Config config;

  private int last;

  @Override
  public String getStatus() {
    return "Opening packs";
  }

  @Override
  public boolean validate() {
    return config.openPacks()
        && last + 3 <= Static.getClient().getTickCount()
        && Inventory.contains((i) -> i.hasAction("Open") && i.getName().contains("pack"));
  }

  @Override
  public void execute() {
    if (Shop.isOpen()) {
      DialogPackets.closeInterface();
    }

    Inventory.getAll((i) -> i.hasAction("Open") && i.getName().contains("pack"))
        .forEach((i) -> i.interact("Open"));
    last = Static.getClient().getTickCount();
  }
}
