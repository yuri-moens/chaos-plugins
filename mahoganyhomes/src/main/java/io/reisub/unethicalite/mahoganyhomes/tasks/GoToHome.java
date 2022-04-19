package io.reisub.unethicalite.mahoganyhomes.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.mahoganyhomes.Config;
import io.reisub.unethicalite.mahoganyhomes.MahoganyHomes;
import io.reisub.unethicalite.utils.api.CMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.coords.WorldPoint;

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
        && Players.getLocal().distanceTo(plugin.getCurrentHome().getLocation()) > 10;
  }

  @Override
  public void execute() {
    if (Players.getLocal().distanceTo(plugin.getCurrentHome().getLocation()) > 50) {
      Item teleport = Inventory.getFirst(plugin.getCurrentHome().getTabletId());
      if (teleport == null && plugin.getCurrentHome().getTabletId() == ItemID.TELEPORT_TO_HOUSE) {
        Item talisman = Inventory.getFirst(ItemID.XERICS_TALISMAN);
        if (talisman == null) {
          return;
        }

        talisman.interact(0); // TODO
      } else if (teleport == null) {
        return;
      } else {
        teleport.interact(0);
      }

      WorldPoint current = Players.getLocal().getWorldLocation();

      Time.sleepTicksUntil(() -> !current.equals(Players.getLocal().getWorldLocation()), 10);
    }

    CMovement.walkTo(plugin.getCurrentHome().getLocation());
  }
}
