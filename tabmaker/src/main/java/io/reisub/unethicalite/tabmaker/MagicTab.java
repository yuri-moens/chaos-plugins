package io.reisub.unethicalite.tabmaker;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MagicTab {
  VARROCK_TELEPORT(23),
  LUMBRIDGE_TELEPORT(12),
  FALADOR_TELEPORT(18),
  HOUSE_TELEPORT(15),
  CAMELOT_TELEPORT(24),
  ARDOUGNE_TELEPORT(19),
  WATCHTOWER_TELEPORT(14),
  ENCHANT_SAPPHIRE(17),
  ENCHANT_EMERALD(16),
  ENCHANT_RUBY(22),
  ENCHANT_DIAMOND(13),
  ENCHANT_DRAGONSTONE(21),
  ENCHANT_ONYX(11),
  BONES_TO_BANANAS(20),
  BONES_TO_PEACHES(25);

  private final int id;
}
