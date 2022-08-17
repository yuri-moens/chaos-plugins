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

    Time.sleepTicksUntil(
        () -> !Inventory.contains("Weak cell", "Medium cell", "Strong cell", "Overcharged cell"),
        10
    );
    if (Inventory.contains(Predicates.ids(Constants.RUNE_IDS))
        && (plugin.getPortalTimer() == -1) || plugin.getPortalTimer() > 8) {
      TileObjects.getNearest("Deposit Pool").interact("Deposit-runes");
      Time.sleepTicks(2);
      Time.sleepTicksUntil(() -> Players.getLocal().isIdle(), 10);
    }
  }
}