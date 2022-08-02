package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
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
    return plugin.getGamePhase() == 10
        &&
        Inventory.contains("Weak cell", "Medium cell", "Strong cell", "Overcharged cell");
  }

  @Override
  public int execute() {
    TileObjects.getNearest(43740, 43741, 43742, 43743).interact("Place-cell");
    Time.sleepTicksUntil(
        () -> !Inventory.contains("Weak cell", "Medium cell", "Strong cell", "Overcharged cell"),
        10);
    TileObjects.getNearest("Deposit Pool").interact("Deposit-runes");
    Time.sleepTicks(2);
    Time.sleepTicksUntil(() -> Players.getLocal().isIdle(), 10);

    return 1;
  }
}