package io.reisub.unethicalite.wintertodt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum InterruptType {
  COLD("Damaged by Wintertodt Cold"),
  SNOWFALL("Damaged by Wintertodt Snowfall"),
  BRAZIER("Brazier Shattered"),
  INVENTORY_FULL("Inventory full of Bruma Roots"),
  OUT_OF_ROOTS("Out of Bruma Roots"),
  FIXED_BRAZIER("Fixed Brazier"),
  LIT_BRAZIER("Lit Brazier"),
  BRAZIER_WENT_OUT("Brazier went out"),
  EAT("You ate"),
  TOO_COLD("Your hands are too cold");

  private final String interruptSourceString;
}
