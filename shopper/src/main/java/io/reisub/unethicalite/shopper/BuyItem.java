package io.reisub.unethicalite.shopper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public class Item {
    private final int id;
    private final int amountToBuy;
    private final int minInShop;

    @Setter
    private int amountBought;
}