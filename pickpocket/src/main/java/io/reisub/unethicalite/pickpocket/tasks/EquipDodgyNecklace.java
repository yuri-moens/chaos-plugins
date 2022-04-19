package io.reisub.unethicalite.pickpocket.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.items.Equipment;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.pickpocket.Config;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;
import net.runelite.api.ItemID;

public class EquipDodgyNecklace extends Task {
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Equipping Dodgy neklace";
  }

  @Override
  public boolean validate() {
    return config.dodgyNecklacesQuantity() > 0
        && (Equipment.fromSlot(EquipmentInventorySlot.AMULET) == null || Equipment.fromSlot(EquipmentInventorySlot.AMULET).getId() != ItemID.DODGY_NECKLACE)
        && Inventory.contains(ItemID.DODGY_NECKLACE);
  }

  @Override
  public void execute() {
    Item necklace = Inventory.getFirst(ItemID.DODGY_NECKLACE);
    if (necklace == null) {
      return;
    }

    necklace.interact("Wear");
    Time.sleepTicksUntil(() -> Equipment.contains(ItemID.DODGY_NECKLACE), 3);
  }
}
