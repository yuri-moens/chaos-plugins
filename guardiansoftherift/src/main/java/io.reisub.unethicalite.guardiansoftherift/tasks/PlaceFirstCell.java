package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;


public class PlaceFirstCell extends Task {
  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Placing first cell at start of game";
  }

  @Override
  public boolean validate() {
    return plugin.getGamePhase() == 3;
  }

  @Override
  public void execute() {
    Time.sleepTick();
    TileObjects.getNearest("Inactive cell tile", "Weak cell tile").interact("Place-cell");
    Time.sleepTicksUntil(() -> !Inventory.contains("Weak cell"), 10);
    plugin.setGamePhase(4);
  }
}