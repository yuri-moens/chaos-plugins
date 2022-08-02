package io.reisub.unethicalite.pickpocket.tasks;

import io.reisub.unethicalite.pickpocket.Config;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;

public class EquipDodgyNecklace extends Task {
  @Inject private Config config;

  @Override
  public String getStatus() {
    return "Equipping Dodgy neklace";
  }

  @Override
  public boolean validate() {
    return config.dodgyNecklacesQuantity() > 0
        && (Equipment.fromSlot(EquipmentInventorySlot.AMULET) == null
            || Equipment.fromSlot(EquipmentInventorySlot.AMULET).getId() != ItemID.DODGY_NECKLACE)
        && Inventory.contains(ItemID.DODGY_NECKLACE);
  }

  @Override
  public int execute() {
    Item necklace = Inventory.getFirst(ItemID.DODGY_NECKLACE);
    if (necklace == null) {
      return 1;
    }

    necklace.interact("Wear");
    Time.sleepTicksUntil(() -> Equipment.contains(ItemID.DODGY_NECKLACE), 3);

    return 1;
  }
}
