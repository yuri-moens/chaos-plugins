package io.reisub.unethicalite.utils.api;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.items.Equipment;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.widgets.Dialog;
import java.util.Collection;
import java.util.function.Predicate;
import net.runelite.api.Item;

public class Interact {
  public static boolean interactWithInventoryOrEquipment(
      int id, String action, String equipmentAction, int optionIndex) {
    return interactWithInventoryOrEquipment(
        (i) -> i.getId() == id, action, equipmentAction, optionIndex);
  }

  public static boolean interactWithInventoryOrEquipment(
      Collection<Integer> ids, String action, String equipmentAction, int optionIndex) {
    return interactWithInventoryOrEquipment(
        Predicates.ids(ids), action, equipmentAction, optionIndex);
  }

  /**
   * Interact with an item that is either in the player's inventory or equipped.
   *
   * @param filter item filter used to search for the item
   * @param action interact action
   * @param equipmentAction optional override for items with different actions when equipped
   * @param optionIndex optional index of the option to pick when the interaction opens an option
   *     dialog, index starts at 1, not at 0
   * @return false if the item wasn't found
   */
  public static boolean interactWithInventoryOrEquipment(
      Predicate<Item> filter, String action, String equipmentAction, int optionIndex) {
    Item item = Inventory.getFirst(filter);
    if (item == null) {
      item = Equipment.getFirst(filter);
      if (item == null) {
        return false;
      }

      if (equipmentAction != null && !equipmentAction.equals("")) {
        item.interact(equipmentAction);
      } else {
        item.interact(action);
      }
    } else {
      item.interact(action);
    }

    if (optionIndex > 0) {
      if (Time.sleepTicksUntil(Dialog::isViewingOptions, 3)) {
        Dialog.chooseOption(optionIndex);
      }
    }

    return true;
  }
}
