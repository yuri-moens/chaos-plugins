package io.reisub.unethicalite.enchanter;

import dev.unethicalite.api.magic.Regular;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnchantSpell {
  LEVEL_1(Regular.LVL_1_ENCHANT),
  LEVEL_2(Regular.LVL_2_ENCHANT),
  LEVEL_3(Regular.LVL_3_ENCHANT),
  LEVEL_4(Regular.LVL_4_ENCHANT),
  LEVEL_5(Regular.LVL_5_ENCHANT),
  LEVEL_6(Regular.LVL_6_ENCHANT),
  LEVEL_7(Regular.LVL_7_ENCHANT);

  private Regular spell;
}
