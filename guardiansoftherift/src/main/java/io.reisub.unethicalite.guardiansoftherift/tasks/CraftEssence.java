package io.reisub.unethicalite.guardiansoftherift.tasks;

import io.reisub.unethicalite.guardiansoftherift.Config;
import io.reisub.unethicalite.guardiansoftherift.GuardiansOfTheRift;
import io.reisub.unethicalite.guardiansoftherift.data.GotrArea;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

public class CraftEssence extends Task {

  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Crafting essence";
  }

  @Override
  public boolean validate() {
    return GotrArea.getCurrent() == GotrArea.MAIN
        && !Inventory.isFull()
        && plugin.getGuardianPower() < config.guardianPowerLastRun()
        && Inventory.contains("Guardian fragments");
  }

  @Override
  public void execute() {
    while (!Inventory.isFull()
        && GotrArea.getCurrent() == GotrArea.MAIN
        && !plugin.isPortalActive()
        && Inventory.contains("Guardian fragments")
        && plugin.getGuardianPower() < config.guardianPowerLastRun()) {
      craft();

      // don't fill pouches during the first inventory
      // don't fill pouches right before starting the last run
      if (!plugin.arePouchesFull()
          && plugin.getElapsedTicks() > 180 / 0.6
          && plugin.getGuardianPower() < config.guardianPowerLastRun()) {
        if (fillPouches()) {
          plugin.setEmptyPouches(0);
          plugin.setFullPouches(4);
        }
      }
    }
  }

  private void craft() {
    final TileObject workbench = TileObjects.getNearest("Workbench");

    if (workbench == null) {
      return;
    }

    workbench.interact("Work-at");
    Time.sleepTicksUntil(() -> Inventory.isFull()
        || GotrArea.getCurrent() != GotrArea.MAIN
        || plugin.isPortalActive()
        || !Inventory.contains("Guardian fragments")
        || plugin.getGuardianPower() >= config.guardianPowerLastRun(), 50);
  }

  private boolean fillPouches() {
    for (Item pouch : Inventory.getAll(Predicates.ids(Constants.ESSENCE_POUCH_IDS))) {
      pouch.interact("Fill");
    }

    Time.sleepTicksUntil(() -> Inventory.getFreeSlots() > 0 || plugin.arePouchesFull(), 3);

    return Inventory.contains(ItemID.GUARDIAN_ESSENCE);
  }
}