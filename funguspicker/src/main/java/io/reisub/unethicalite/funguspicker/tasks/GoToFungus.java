package io.reisub.unethicalite.funguspicker.tasks;

import io.reisub.unethicalite.funguspicker.FungusPicker;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.api.Interact;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Tab;
import net.unethicalite.api.widgets.Tabs;

public class GoToFungus extends Task {
  @Inject private FungusPicker plugin;

  @Override
  public String getStatus() {
    return "Going to fungus location";
  }

  @Override
  public boolean validate() {
    return !Inventory.contains(ItemID.MORT_MYRE_FUNGUS)
        && Players.getLocal().distanceTo(FungusPicker.FUNGUS_LOCATION) > 10;
  }

  @Override
  public void execute() {
    int regionId = Players.getLocal().getWorldLocation().getRegionID();

    if (!FungusPicker.VER_SINHAZA_REGION_IDS.contains(regionId)) {
      boolean interacted =
          Interact.interactWithInventoryOrEquipment(
              ItemID.DRAKANS_MEDALLION, "Ver Sinhaza", null, 0);

      if (!interacted) {
        plugin.stop("Couldn't find Drakan's medallion. Stopping plugin.");
        return;
      }

      Time.sleepTicksUntil(
          () ->
              FungusPicker.VER_SINHAZA_REGION_IDS.contains(
                  Players.getLocal().getWorldLocation().getRegionID()),
          10);
    }

    ChaosMovement.walkTo(
        FungusPicker.FUNGUS_LOCATION,
        () -> {
          if (Tabs.isOpen(Tab.EQUIPMENT)) {
            Tabs.openInterface(Tab.INVENTORY);
          }

          Inventory.getAll(Predicates.ids(Constants.DUELING_RING_IDS))
              .forEach(
                  (i) -> {
                    i.interact("Wear");
                    Tabs.openInterface(Tab.EQUIPMENT);
                  });
        });
  }
}
