package io.reisub.unethicalite.shopper.tasks;

import io.reisub.unethicalite.shopper.BuyItem;
import io.reisub.unethicalite.shopper.Shopper;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.items.Shop;

public class Buy extends Task {
  private final Map<BuyItem, Integer> buyMap = new HashMap<>();
  @Inject private Shopper plugin;

  @Override
  public String getStatus() {
    return "Buying";
  }

  @Override
  public boolean validate() {
    if (!Shop.isOpen()) {
      return false;
    }

    int freeSlots = Inventory.getFreeSlots();
    boolean itemsLeftToBuy = false;
    buyMap.clear();

    for (BuyItem buyItem : plugin.getBuyItems()) {
      int toBuy = buyItem.getAmountToBuy() - buyItem.getAmountBought();

      if (toBuy <= 0) {
        continue;
      }

      itemsLeftToBuy = true;

      int canBuy = Shop.getStock(buyItem.getId()) - buyItem.getMinInShop();

      if (canBuy <= 0) {
        continue;
      }

      toBuy = Math.min(canBuy, toBuy);

      buyMap.put(buyItem, toBuy);

      if (buyItem.isStackable()) {
        freeSlots--;
      } else {
        freeSlots -= toBuy;
      }

      if (freeSlots == 0) {
        break;
      }
    }

    if (!itemsLeftToBuy) {
      plugin.stop("Bought all items. Stopping plugin.");
    }

    if (buyMap.isEmpty()) {
      plugin.setHop(true);
    }

    return !buyMap.isEmpty() && itemsLeftToBuy && !Inventory.isFull();
  }

  @Override
  public void execute() {
    int freeSlots = Inventory.getFreeSlots();

    for (Map.Entry<BuyItem, Integer> order : buyMap.entrySet()) {
      int itemId = order.getKey().getId();
      boolean stackable = order.getKey().isStackable();
      int amountToBuy = order.getValue();

      if (amountToBuy >= freeSlots && !stackable) {
        Shop.buyFifty(itemId);
        order.getKey().bought(freeSlots);
        freeSlots = 0;
        break;
      }

      if (stackable && !Inventory.contains(itemId)) {
        freeSlots--;
      }

      while (amountToBuy >= 50) {
        Shop.buyFifty(itemId);
        amountToBuy -= 50;
        order.getKey().bought(50);
      }

      while (amountToBuy >= 10) {
        Shop.buyTen(itemId);
        amountToBuy -= 10;
        order.getKey().bought(10);

        if (!stackable) {
          freeSlots -= 10;
        }
      }

      while (amountToBuy >= 5) {
        Shop.buyFive(itemId);
        amountToBuy -= 5;
        order.getKey().bought(5);

        if (!stackable) {
          freeSlots -= 5;
        }
      }

      while (amountToBuy >= 1) {
        Shop.buyOne(itemId);
        amountToBuy -= 1;
        order.getKey().bought(1);

        if (!stackable) {
          freeSlots -= 1;
        }
      }
    }

    if (freeSlots > 0) {
      plugin.setHop(true);
    }
  }
}
