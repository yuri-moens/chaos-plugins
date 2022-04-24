package io.reisub.unethicalite.pickpocket.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.ItemID;

public class ClearInventory extends Task {
  @Override
  public String getStatus() {
    return "Clearing inventory";
  }

  @Override
  public boolean validate() {
    if (Inventory.getCount(true, "Coin pouch") == 28) {
      return true;
    }

    return (Inventory.isFull() || Players.getLocal().getModelHeight() == 1000)
        && (Inventory.contains("Coin pouch")
            || Inventory.contains(
                ItemID.DEATH_RUNE,
                ItemID.BLOOD_RUNE,
                ItemID.BLOOD_PINT,
                ItemID.COOKED_MYSTERY_MEAT,
                ItemID.JUG));
  }

  @Override
  public void execute() {
    Item coinPouch = Inventory.getFirst("Coin pouch");

    if (coinPouch != null) {
      coinPouch.interact("Open-all");
    }

    Item deathRune = Inventory.getFirst(ItemID.DEATH_RUNE);
    Item bloodRune = Inventory.getFirst(ItemID.BLOOD_RUNE);
    Item runePouch = Inventory.getFirst(ItemID.RUNE_POUCH);

    if (runePouch != null) {
      if (deathRune != null) {
        deathRune.useOn(runePouch);
      }

      if (bloodRune != null) {
        bloodRune.useOn(runePouch);
      }
    }

    Inventory.getAll(ItemID.COOKED_MYSTERY_MEAT)
        .forEach(
            i -> {
              i.interact(1);
              Time.sleepTicks(3);
            });

    Inventory.getAll(ItemID.BLOOD_PINT).forEach(Item::drop);
    Inventory.getAll(ItemID.JUG).forEach(Item::drop);
  }
}
