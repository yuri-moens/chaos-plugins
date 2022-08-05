package io.reisub.unethicalite.fletching.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Product {
  ARROW_SHAFTS(1),
  HEADLESS_ARROWS(),
  ARROWS(),
  DARTS(),
  BOLTS(),
  BOLTS_TIPPED(),
  SHORTBOW_U(2),
  SHORTBOW(2),
  LONGBOW_U(3),
  LONGBOW(3),
  ;

  private final int productionIndex;

  Product() {
    this(-1);
  }
}
