package io.reisub.unethicalite.combathelper.prayer;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Hitsplat;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldArea;

class MemorizedPlayer {
  @Getter(AccessLevel.PACKAGE)
  private final Player player;

  @Getter(AccessLevel.PACKAGE)
  private final List<Hitsplat> recentHitsplats;

  @Getter(AccessLevel.PACKAGE)
  @Setter(AccessLevel.PACKAGE)
  private WorldArea lastWorldArea;

  MemorizedPlayer(final Player player) {
    this.player = player;
    this.recentHitsplats = new ArrayList<>();
  }
}
