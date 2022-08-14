package io.reisub.unethicalite.barbarianassault.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Call {
  // collector calls
  ACCURATE("Accurate", "Tell-accurate"),
  AGGRESSIVE("Aggressive", "Tell-aggressive"),
  CONTROLLED("Controlled", "Tell-controlled"),
  DEFENSIVE("Defensive", "Tell-defensive"),

  // healer calls
  TOFU("Tofu", "Tell-tofu"),
  CRACKERS("Crackers", "Tell-crackers"),
  WORMS("Worms", "Tell-worms"),

  // attacker calls
  RED("Red egg", "Tell-red"),
  GREEN("Green egg", "Tell-green"),
  BLUE("Blue egg", "Tell-blue"),

  // defender calls
  POISONED_TOFU("Pois. Tofu", "Tell-tofu"),
  POISONED_MEAT("Pois. Meat", "Tell-meat"),
  POISONED_WORMS("Pois. Worms", "Tell-worms");

  private final String widgetString;
  private final String action;
}
