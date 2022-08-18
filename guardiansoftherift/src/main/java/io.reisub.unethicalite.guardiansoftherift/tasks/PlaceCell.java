package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.guardiansoftherift.data.GotrArea;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class PlaceCell extends Task {

  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Placing cell";
  }

  @Override
  public boolean validate() {
    return GotrArea.getCurrent() == GotrArea.MAIN
        && plugin.getElapsedTicks() != -1
        && Inventory.contains("Weak cell", "Medium cell", "Strong cell", "Overcharged cell");
  }

  @Override
  public void execute() {
    TileObjects.getNearest(43740, 43741, 43742, 43743).interact("Place-cell");

    if (!Time.sleepTicksUntil(() -> Players.getLocal().isMoving(), 2)) {
      return;
    }

    // if we stop moving and we still have a cell after 3 ticks it means the cell tile
    // has changed and our action stops so we return as quickly as possible
    Time.sleepTicksUntil(() -> !Players.getLocal().isMoving(), 10);
    if (!Time.sleepTicksUntil(
        () -> !Inventory.contains("Weak cell", "Medium cell", "Strong cell", "Overcharged cell")
            || !Players.getLocal().isMoving(),
        3
    )) {
      return;
    }

    if (Inventory.contains(Predicates.ids(Constants.RUNE_IDS))
        && !Inventory.contains("Weak cell", "Medium cell", "Strong cell", "Overcharged cell")
        && (plugin.getPortalTimer() == -1) || plugin.getPortalTimer() > 8) {
      TileObjects.getNearest("Deposit Pool").interact("Deposit-runes");
      Time.sleepTicksUntil(() -> !Inventory.contains(Predicates.ids(Constants.RUNE_IDS)), 15);
    }
  }
}