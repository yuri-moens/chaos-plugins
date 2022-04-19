package io.reisub.unethicalite.combathelper.bones;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.ItemID;

@AllArgsConstructor
@Getter
public enum Ashes {
  NONE(-1),
  FIENDISH_ASHES(ItemID.FIENDISH_ASHES),
  VILE_ASHES(ItemID.VILE_ASHES),
  MALICIOUS_ASHES(ItemID.MALICIOUS_ASHES),
  ABYSSAL_ASHES(ItemID.ABYSSAL_ASHES),
  INFERNAL_ASHES(ItemID.INFERNAL_ASHES);

  private final int id;

  static Ashes[] allBelow(Ashes ashes) {
    if (ashes == NONE) {
      return new Ashes[0];
    }

    Ashes[] allBelow = new Ashes[ashes.ordinal()];
    int i = 0;

    for (Ashes a : Ashes.values()) {
      if (a == NONE) {
        continue;
      }

      allBelow[i++] = a;

      if (a == ashes) {
        break;
      }
    }

    return allBelow;
  }
}
