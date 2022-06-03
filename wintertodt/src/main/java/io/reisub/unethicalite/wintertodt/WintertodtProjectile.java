package io.reisub.unethicalite.wintertodt;

import net.unethicalite.api.coords.Area;
import net.unethicalite.api.coords.RectangularArea;
import java.time.Instant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class WintertodtProjectile {

  private final int x;
  private final int y;
  private final boolean aoe;
  private final Instant start;

  public Area getDamageArea() {
    if (aoe) {
      return new RectangularArea(x - 1, y - 1, x + 1, y + 1);
    } else {
      if (x == 1638) {
        return new RectangularArea(x, y - 1, x + 3, y + 2);
      } else {
        return new RectangularArea(x - 1, y - 1, x + 2, y + 2);
      }
    }
  }
}
