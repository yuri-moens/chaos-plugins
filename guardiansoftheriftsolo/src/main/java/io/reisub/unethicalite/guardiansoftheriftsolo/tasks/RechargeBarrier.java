package io.reisub.unethicalite.guardiansoftheriftsolo.tasks;

import io.reisub.unethicalite.guardiansoftheriftsolo.GuardiansOfTheRiftSolo;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.AreaType;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.CellTile;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.CellType;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;

@Slf4j
public class RechargeBarrier extends Task {

  @Inject
  private GuardiansOfTheRiftSolo plugin;

  @Override
  public String getStatus() {
    return "Recharging barrier";
  }

  @Override
  public boolean validate() {
    return plugin.isGameActive()
        && plugin.getElapsedTicks() >= 115 / 0.6
        && AreaType.getCurrent() == AreaType.MAIN
        && Inventory.contains(Predicates.ids(Constants.CELL_IDS));
  }

  @Override
  public void execute() {
    if (plugin.isNearStart()) {
      Movement.walk(CellTile.NORTH.getLocation().dy(-2));
      Time.sleepTicks(4);
    }

    final TileObject cellTile = getCellTile().getObject();

    if (cellTile == null) {
      return;
    }

    cellTile.interact("Place-cell");
    Time.sleepTicksUntil(() -> !Inventory.contains(Predicates.ids(Constants.CELL_IDS)), 20);
  }

  private CellTile getCellTile() {
    if (plugin.getElapsedTicks() >= 115 / 0.6 && plugin.getElapsedTicks() <= 119 / 0.6) {
      return CellTile.NORTH;
    }

    final CellTile lowestHp = CellTile.getLowestHealth();
    final CellTile lowestType = CellTile.getLowestType();

    if (lowestHp.getHealth() < 20) {
      log.info("recharging lowest hp because almost dead: " + lowestHp.getHealth());
      return lowestHp;
    }

    if (lowestType.getType().ordinal() < CellType.getTypeInInventory().ordinal()) {
      log.info("recharging lowest type: " + lowestType.getType());
      return lowestType;
    }

    log.info("recharging lowest hp: " + lowestHp.getHealth());
    return lowestHp;
  }
}
