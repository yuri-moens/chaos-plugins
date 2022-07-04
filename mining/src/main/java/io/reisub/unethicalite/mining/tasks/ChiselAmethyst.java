package io.reisub.unethicalite.mining.tasks;

import io.reisub.unethicalite.mining.Config;
import io.reisub.unethicalite.mining.Location;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Production;

public class ChiselAmethyst extends Task {

  @Inject
  private Config config;
  @Inject
  private Mine mineTask;

  @Override
  public String getStatus() {
    return "Chiseling";
  }

  @Override
  public boolean validate() {
    return config.location() == Location.AMETHYST_EAST
        && Inventory.isFull()
        && Inventory.contains(ItemID.CHISEL);
  }

  @Override
  public void execute() {
    mineTask.setCurrentRockPosition(null);

    final Item chisel = Inventory.getFirst(ItemID.CHISEL);
    final Item amethyst = Inventory.getFirst(ItemID.AMETHYST);

    if (chisel == null || amethyst == null) {
      return;
    }

    chisel.useOn(amethyst);

    Time.sleepTicksUntil(Production::isOpen, 5);

    Production.chooseOption(config.chiselProduct().getProductionIndex());

    Time.sleepTicksUntil(() -> !Inventory.contains(ItemID.AMETHYST), 60);
  }
}
