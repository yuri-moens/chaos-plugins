package io.reisub.unethicalite.mining;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ChiselProduct {
  BOLT_TIPS(1),
  ARROW_TIPS(2),
  JAVELIN_HEADS(3),
  DART_TIPS(4);

  private final int productionIndex;
}
