package io.reisub.unethicalite.guardiansoftheriftsolo.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ObjectID;

@RequiredArgsConstructor
@Getter
public enum RuneType {
  ELEMENTAL(ObjectID.ESSENCE_PILE_ELEMENTAL),
  CATALYTIC(ObjectID.ESSENCE_PILE_CATALYTIC);

  private final int guardianPileId;
}
