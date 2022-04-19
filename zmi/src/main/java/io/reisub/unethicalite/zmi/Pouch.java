package io.reisub.unethicalite.zmi;

import net.runelite.api.ItemID;

public enum Pouch {
  GIANT(12, 9),
  LARGE(9, 7),
  MEDIUM(6, 3),
  SMALL(3);

  private final int baseHoldAmount;
  private final int degradedBaseHoldAmount;

  private int holding;
  private boolean degraded;

  Pouch(int holdAmount) {
    this(holdAmount, -1);
  }

  Pouch(int holdAmount, int degradedHoldAmount) {
    this.baseHoldAmount = holdAmount;
    this.degradedBaseHoldAmount = degradedHoldAmount;
  }

  public static Pouch forItem(int itemId) {
    switch (itemId) {
      case ItemID.SMALL_POUCH:
        return SMALL;
      case ItemID.MEDIUM_POUCH:
      case ItemID.MEDIUM_POUCH_5511:
        return MEDIUM;
      case ItemID.LARGE_POUCH:
      case ItemID.LARGE_POUCH_5513:
        return LARGE;
      case ItemID.GIANT_POUCH:
      case ItemID.GIANT_POUCH_5515:
        return GIANT;
      default:
        return null;
    }
  }

  public int getHoldAmount() {
    return degraded ? degradedBaseHoldAmount : baseHoldAmount;
  }

  public int getRemaining() {
    int holdAmount = degraded ? degradedBaseHoldAmount : baseHoldAmount;
    return holdAmount - holding;
  }

  public void addHolding(int delta) {
    holding += delta;

    int holdAmount = degraded ? degradedBaseHoldAmount : baseHoldAmount;
    if (holding < 0) {
      holding = 0;
    }

    if (holding > holdAmount) {
      holding = holdAmount;
    }
  }

  public void degrade(boolean state) {
    if (state != degraded) {
      degraded = state;
      int holdAmount = degraded ? degradedBaseHoldAmount : baseHoldAmount;
      holding = Math.min(holding, holdAmount);
    }
  }
}
