package io.reisub.unethicalite.sulliuscep.tasks;

import io.reisub.unethicalite.sulliuscep.Sulliuscep;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.client.Static;

public class Cut extends Task {

  @Inject
  private Sulliuscep plugin;
  @Inject
  private CurePoison curePoisonTask;
  @Inject
  private Eat eatTask;

  @Override
  public String getStatus() {
    return "Cutting sulliuscep";
  }

  @Override
  public boolean validate() {
    return !Inventory.isFull()
        && (!Players.getLocal().isAnimating()
        || Static.getClient().getTickCount() - plugin.getLastDrop() <= 1)
        && plugin.getCurrentSulliuscep().isReachable();
  }

  @Override
  public void execute() {
    final TileObject sulliuscep = plugin.getCurrentSulliuscep().getObject();

    if (sulliuscep == null) {
      return;
    }

    if (Combat.getSpecEnergy() == 100 && Equipment.contains(ItemID.DRAGON_AXE)) {
      Combat.toggleSpec();
    }

    GameThread.invoke(() -> sulliuscep.interact("Cut"));
    Time.sleepTicksUntil(() -> Players.getLocal().isAnimating()
        || Combat.isPoisoned()
        || Combat.getCurrentHealth() < 30, 35);

    if (Combat.isPoisoned()) {
      curePoisonTask.execute();
      Time.sleepTick();

      GameThread.invoke(() -> sulliuscep.interact("Cut"));
      Time.sleepTicks(5);

      Time.sleepTicksUntil(() -> Players.getLocal().isAnimating(), 35);
    }

    if (Combat.getCurrentHealth() < 30) {
      eatTask.execute();
      Time.sleepTick();

      GameThread.invoke(() -> sulliuscep.interact("Cut"));
      Time.sleepTicks(5);

      Time.sleepTicksUntil(() -> Players.getLocal().isAnimating(), 35);
    }

    Time.sleepTick();
  }
}
