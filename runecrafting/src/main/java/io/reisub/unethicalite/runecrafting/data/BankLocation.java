package io.reisub.unethicalite.runecrafting.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.coords.WorldPoint;

@RequiredArgsConstructor
@Getter
public enum BankLocation {
  CRAFTING_GUILD(new WorldPoint(2934, 3282, 0)),
  ;

  private final WorldPoint bankPoint;
}
