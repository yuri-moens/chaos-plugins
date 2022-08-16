package io.reisub.unethicalite.guardiansoftheriftsolo.tasks;

import io.reisub.unethicalite.guardiansoftheriftsolo.GuardiansOfTheRiftSolo;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.AreaType;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.RuneType;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class BuildGuardian extends Task {

  @Inject
  private GuardiansOfTheRiftSolo plugin;

  @Override
  public String getStatus() {
    return "Building guardian";
  }

  @Override
  public boolean validate() {
    if (!plugin.isGameActive()
        || AreaType.getCurrent() != AreaType.MAIN
        || !Inventory.contains(Predicates.ids(Constants.CELL_IDS))
        || plugin.getGuardianCount() == 10) {
      return false;
    }

    if (plugin.getGuardianPower() >= 60) {
      return false;
    }

    // build our first guardian
    if (plugin.getElapsedTicks() > 70 / 0.6
        && plugin.getGuardianCount() == 0) {
      return true;
    }

    // build a second guardian if we have a strong or overcharged cell as our first cell
    if (Inventory.contains(ItemID.STRONG_CELL, ItemID.OVERCHARGED_CELL)
        && plugin.getElapsedTicks() < 150 / 0.6) {
      return true;
    }

    // build a second guardian if we ever have less than 2 after 150 seconds
    return plugin.getElapsedTicks() > 150 / 0.6 && plugin.getGuardianCount() < 2;
  }

  @Override
  public void execute() {
    final RuneType runeType =
        plugin.getLastGuardianBuild() == null || plugin.getLastGuardianBuild() == RuneType.CATALYTIC
            ? RuneType.ELEMENTAL
            : RuneType.CATALYTIC;

    final TileObject essencePile = TileObjects.getNearest(runeType.getGuardianPileId());

    if (essencePile == null) {
      return;
    }

    final int guardianCount = plugin.getGuardianCount();

    essencePile.interact("Assemble");
    if (!Time.sleepTicksUntil(() -> plugin.getGuardianCount() > guardianCount, 20)) {
      return;
    }

    plugin.setLastGuardianBuild(runeType);
  }
}
