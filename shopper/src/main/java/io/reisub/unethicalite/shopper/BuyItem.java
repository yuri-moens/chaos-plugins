package io.reisub.unethicalite.shopper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public class BuyItem {
  private final int id;
  private final int amountToBuy;
  private final int minInShop;
  private final boolean stackable;

  @Setter private int amountBought;

  public void bought(int amount) {
    amountBought += amount;
  }
}
