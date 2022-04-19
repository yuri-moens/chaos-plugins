package io.reisub.unethicalite.shopper.tasks;

import dev.unethicalite.api.commons.Rand;
import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.NPCs;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.items.Shop;
import dev.unethicalite.api.movement.Movement;
import io.reisub.unethicalite.shopper.Config;
import io.reisub.unethicalite.shopper.Shopper;
import io.reisub.unethicalite.utils.api.CMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.NPC;

public class OpenShop extends Task {
  @Inject
  private Shopper plugin;

  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Opening shop";
  }

  @Override
  public boolean validate() {
    return !Inventory.isFull()
        && !Shop.isOpen();
  }

  @Override
  public void execute() {
    if (!Movement.isRunEnabled() && Movement.getRunEnergy() >= Rand.nextInt(20, 40)) {
      Movement.toggleRun();
    }

    if (plugin.getNpcLocation() != null) {
      CMovement.walkTo(plugin.getNpcLocation(), 1);
    }

    NPC npc = NPCs.getNearest(config.npcName());
    if (npc == null) {
      return;
    }

    GameThread.invoke(() -> npc.interact("Trade"));
    Time.sleepTicksUntil(Shop::isOpen, 20);
  }
}
