package io.reisub.unethicalite.herblore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.ItemID;

@AllArgsConstructor
@Getter
public enum Base {
  VIAL_OF_WATER(ItemID.VIAL_OF_WATER),
  VIAL_OF_BLOOD(ItemID.VIAL_OF_BLOOD),
  COCONUT_MILK(ItemID.COCONUT_MILK);

  private final int id;
}
