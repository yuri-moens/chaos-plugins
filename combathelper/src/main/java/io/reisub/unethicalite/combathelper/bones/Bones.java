package io.reisub.unethicalite.combathelper.bones;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.ItemID;

@AllArgsConstructor
@Getter
public enum Bones {
  NONE(-1),
  BONES(ItemID.BONES),
  WOLF_BONES(ItemID.WOLF_BONES),
  BURNT_BONES(ItemID.BURNT_BONES),
  MONKEY_BONES(ItemID.MONKEY_BONES),
  BAT_BONES(ItemID.BAT_BONES),
  BIG_BONES(ItemID.BIG_BONES),
  JOGRE_BONES(ItemID.JOGRE_BONES),
  ZOGRE_BONES(ItemID.ZOGRE_BONES),
  SHAIKAHAN_BONES(ItemID.SHAIKAHAN_BONES),
  BABYDRAGON_BONES(ItemID.BABYDRAGON_BONES),
  WYRM_BONES(ItemID.WYRM_BONES),
  WYVERN_BONES(ItemID.WYVERN_BONES),
  DRAGON_BONES(ItemID.DRAGON_BONES),
  DRAKE_BONES(ItemID.DRAKE_BONES),
  FAYRG_BONES(ItemID.FAYRG_BONES),
  LAVA_DRAGON_BONES(ItemID.LAVA_DRAGON_BONES),
  RAURG_BONES(ItemID.RAURG_BONES),
  HYDRA_BONES(ItemID.HYDRA_BONES),
  DAGANNOTH_BONES(ItemID.DAGANNOTH_BONES),
  OURG_BONES(ItemID.OURG_BONES),
  SUPERIOR_DRAGON_BONES(ItemID.SUPERIOR_DRAGON_BONES);

  private final int id;

  static Bones[] allBelow(Bones bones) {
    if (bones == NONE) {
      return new Bones[0];
    }

    Bones[] allBelow = new Bones[bones.ordinal()];
    int i = 0;

    for (Bones b : Bones.values()) {
      if (b == NONE) {
        continue;
      }

      allBelow[i++] = b;

      if (b == bones) {
        break;
      }
    }

    return allBelow;
  }
}
