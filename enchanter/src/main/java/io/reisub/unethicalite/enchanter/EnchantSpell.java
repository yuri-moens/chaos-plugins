package io.reisub.unethicalite.enchanter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.unethicalite.api.magic.Spell;
import net.unethicalite.api.magic.SpellBook;

@AllArgsConstructor
@Getter
public enum EnchantSpell {
  LEVEL_1(SpellBook.Standard.LVL_1_ENCHANT),
  LEVEL_2(SpellBook.Standard.LVL_2_ENCHANT),
  LEVEL_3(SpellBook.Standard.LVL_3_ENCHANT),
  LEVEL_4(SpellBook.Standard.LVL_4_ENCHANT),
  LEVEL_5(SpellBook.Standard.LVL_5_ENCHANT),
  LEVEL_6(SpellBook.Standard.LVL_6_ENCHANT),
  LEVEL_7(SpellBook.Standard.LVL_7_ENCHANT);

  private final Spell spell;
}
