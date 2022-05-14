package io.reisub.unethicalite.mahoganyhomes.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.movement.Reachable;
import io.reisub.unethicalite.mahoganyhomes.Config;
import io.reisub.unethicalite.mahoganyhomes.Home;
import io.reisub.unethicalite.mahoganyhomes.MahoganyHomes;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.api.Interact;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;

public class GoToHome extends Task {

  @Inject
  private MahoganyHomes plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Going to home";
  }

  @Override
  public boolean validate() {
    return plugin.getCurrentHome() != null
        && !plugin.getCurrentHome().isInHome(Players.getLocal());
  }

  @Override
  public void execute() {
    teleport();

    if (!Utils.isInRegion(plugin.getCurrentHome().getRegions())) {
      return;
    }

    if (Players.getLocal().getWorldLocation().getPlane() > 0) {
      useStairs();
    }

    ChaosMovement.walkTo(plugin.getCurrentHome().getLocation(), 0, null, 100, 2);

    Time.sleepTicksUntil(() -> plugin.getCurrentHome().isInHome(Players.getLocal()), 30);

//    if (plugin.getCurrentHome() == Home.TAU) {
//      Time.sleepTicks(3);
//    }
  }

  private void teleport() {
    final Home home = plugin.getCurrentHome();

    if (Utils.isInRegion(home.getRegions())) {
      return;
    }

    final Item tab = Inventory.getFirst(home.getTabletId());

    if (tab == null) {
      if (home.getTabletId() == ItemID.TELEPORT_TO_HOUSE) {
        Interact.interactWithInventoryOrEquipment(
            ItemID.XERICS_TALISMAN,
            "Rub",
            "Xeric's Glade",
            4
        );
      } else {
        plugin.stop("Couldn't find teleport tab. Stopping plugin.");
        return;
      }
    } else {
      tab.interact("Break");
    }

    Time.sleepTicksUntil(() -> Utils.isInRegion(home.getRegions()), 10);
    Time.sleepTicks(2);
  }

  private void useStairs() {
    final TileObject stairs = TileObjects.getNearest(
        o -> o.hasAction("Climb-up", "Climb-down")
    );

    if (stairs == null) {
      return;
    }

    while (!Reachable.isInteractable(stairs)) {
      ChaosMovement.openDoor(stairs);
    }

    final int plane = Players.getLocal().getWorldLocation().getPlane();

    if (stairs.hasAction("Climb-up")) {
      GameThread.invoke(() -> stairs.interact("Climb-up"));
    } else if (stairs.hasAction("Climb-down")) {
      GameThread.invoke(() -> stairs.interact("Climb-down"));
    }

    Time.sleepTicksUntil(() -> plane != Players.getLocal().getWorldLocation().getPlane(), 20);
  }
}
