package io.reisub.unethicalite.giantsfoundry.enums;

import static io.reisub.unethicalite.giantsfoundry.enums.CommissionType.BROAD;
import static io.reisub.unethicalite.giantsfoundry.enums.CommissionType.FLAT;
import static io.reisub.unethicalite.giantsfoundry.enums.CommissionType.HEAVY;
import static io.reisub.unethicalite.giantsfoundry.enums.CommissionType.LIGHT;
import static io.reisub.unethicalite.giantsfoundry.enums.CommissionType.NARROW;
import static io.reisub.unethicalite.giantsfoundry.enums.CommissionType.SPIKED;
import static io.reisub.unethicalite.giantsfoundry.enums.MouldType.BLADE;
import static io.reisub.unethicalite.giantsfoundry.enums.MouldType.FORTE;
import static io.reisub.unethicalite.giantsfoundry.enums.MouldType.TIP;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.game.Vars;
import net.unethicalite.api.widgets.Widgets;

@AllArgsConstructor
public enum Mould {
  CHOPPER_FORTE("Chopper Forte", FORTE, ImmutableMap.of(BROAD, 4, LIGHT, 4, FLAT, 4), 0),
  GALDIUS_RICASSO("Galdius Ricasso", FORTE, ImmutableMap.of(BROAD, 4, HEAVY, 4, FLAT, 4), 0),
  DISARMING_FORTE("Disarming Forte", FORTE, ImmutableMap.of(NARROW, 4, LIGHT, 4, SPIKED, 4), 0),
  MEDUSA_RICASSO("Medusa Ricasso", FORTE, ImmutableMap.of(BROAD, 8, HEAVY, 6, FLAT, 8), 0),
  SERPENT_RICASSO("Serpent Ricasso", FORTE, ImmutableMap.of(NARROW, 6, LIGHT, 8, FLAT, 8), 0),
  SERRATED_FORTE("Serrated Forte", FORTE, ImmutableMap.of(NARROW, 8, HEAVY, 8, SPIKED, 6), 0),
  STILETTO_FORTE("Stiletto Forte", FORTE, ImmutableMap.of(NARROW, 8, LIGHT, 10, FLAT, 8), 13916),
  DEFENDER_BASE("Defender Base", FORTE, ImmutableMap.of(BROAD, 8, HEAVY, 10, FLAT, 8), 13917),
  JUGGERNAUT_FORTE("Juggernaut Forte", FORTE, ImmutableMap.of(BROAD, 4, HEAVY, 4, SPIKED, 16),
      13918),
  CHOPPER_FORTE_1("Chopper Forte +1", FORTE, ImmutableMap.of(BROAD, 3, LIGHT, 4, FLAT, 18), 13919),
  SPIKER("Spiker!", FORTE, ImmutableMap.of(NARROW, 1, HEAVY, 2, SPIKED, 22), 13920),

  SAW_BLADE("Saw Blade", BLADE, ImmutableMap.of(BROAD, 4, LIGHT, 4, SPIKED, 4), 0),
  DEFENDERS_EDGE("Defenders Edge", BLADE, ImmutableMap.of(BROAD, 4, HEAVY, 4, SPIKED, 4), 0),
  FISH_BLADE("Fish Blade", BLADE, ImmutableMap.of(NARROW, 4, LIGHT, 4, FLAT, 4), 0),
  MEDUSA_BLADE("Medusa Blade", BLADE, ImmutableMap.of(BROAD, 8, HEAVY, 8, FLAT, 6), 0),
  STILETTO_BLADE("Stiletto Blade", BLADE, ImmutableMap.of(NARROW, 8, LIGHT, 6, FLAT, 8), 0),
  GLADIUS_EDGE("Gladius Edge", BLADE, ImmutableMap.of(NARROW, 6, HEAVY, 8, FLAT, 8), 0),
  FLAMBERGE_BLADE("Flamberge Blade", BLADE, ImmutableMap.of(NARROW, 8, LIGHT, 8, SPIKED, 10),
      13921),
  SERPENT_BLADE("Serpent Blade", BLADE, ImmutableMap.of(NARROW, 10, LIGHT, 8, FLAT, 8), 13922),
  CLAYMORE_BLADE("Claymore Blade", BLADE, ImmutableMap.of(BROAD, 16, HEAVY, 4, FLAT, 4), 13923),
  FLEUR_DE_BLADE("Fleur de Blade", BLADE, ImmutableMap.of(BROAD, 4, HEAVY, 18, SPIKED, 1), 13924),
  CHOPPA("Choppa!", BLADE, ImmutableMap.of(BROAD, 1, LIGHT, 22, FLAT, 2), 13925),

  PEOPLE_POKER_POINT("People Poker Point", TIP, ImmutableMap.of(NARROW, 4, HEAVY, 4, FLAT, 4), 0),
  CHOPPER_TIP("Chopper Tip", TIP, ImmutableMap.of(BROAD, 4, LIGHT, 4, SPIKED, 4), 0),
  MEDUSAS_HEAD("Medusa's Head", TIP, ImmutableMap.of(BROAD, 4, HEAVY, 4, SPIKED, 4), 0),
  SERPENTS_FANG("Serpent's Fang", TIP, ImmutableMap.of(NARROW, 8, LIGHT, 6, SPIKED, 8), 0),
  GLADIUS_POINT("Gladius Point", TIP, ImmutableMap.of(NARROW, 8, HEAVY, 8, FLAT, 6), 0),
  SAW_TIP("Saw Tip", TIP, ImmutableMap.of(BROAD, 6, HEAVY, 8, SPIKED, 8), 0),
  CORRUPTED_POINT("Corrupted Point", TIP, ImmutableMap.of(NARROW, 8, LIGHT, 10, SPIKED, 8), 13926),
  DEFENDERS_TIP("Defenders Tip", TIP, ImmutableMap.of(BROAD, 10, HEAVY, 8, SPIKED, 8), 13927),
  SERRATED_TIPS("Serrated Tips", TIP, ImmutableMap.of(NARROW, 4, LIGHT, 16, SPIKED, 4), 13928),
  NEEDLE_POINT("Needle Point", TIP, ImmutableMap.of(NARROW, 18, LIGHT, 3, FLAT, 4), 13929),
  THE_POINT("The Point!", TIP, ImmutableMap.of(BROAD, 2, LIGHT, 1, FLAT, 22), 13930),
  ;

  public static final Mould[] values = Mould.values();
  private final String name;
  private final MouldType mouldType;
  private final Map<CommissionType, Integer> typeToScore;
  private final int unlockedVarbit;

  public static Mould forName(String text) {
    for (Mould mould : values) {
      if (mould.name.equalsIgnoreCase(text)) {
        return mould;
      }
    }
    return null;
  }

  public static List<Mould> getFortes() {
    List<Mould> fortes = new ArrayList<>(11);

    for (Mould mould : Mould.values()) {
      if (mould.mouldType == FORTE) {
        fortes.add(mould);
      }
    }

    return fortes;
  }

  public static List<Mould> getBlades() {
    List<Mould> blades = new ArrayList<>(11);

    for (Mould mould : Mould.values()) {
      if (mould.mouldType == BLADE) {
        blades.add(mould);
      }
    }

    return blades;
  }

  public static List<Mould> getTips() {
    List<Mould> tips = new ArrayList<>(11);

    for (Mould mould : Mould.values()) {
      if (mould.mouldType == TIP) {
        tips.add(mould);
      }
    }

    return tips;
  }

  public int getScore(CommissionType type1, CommissionType type2) {
    int score = 0;
    score += typeToScore.getOrDefault(type1, 0);
    score += typeToScore.getOrDefault(type2, 0);
    return score;
  }

  public boolean isUnlocked() {
    return unlockedVarbit == 0 || Vars.getBit(unlockedVarbit) == 1;
  }

  public Widget getWidget() {
    final Widget mouldParent = Widgets.fromId(47054857);

    if (!Widgets.isVisible(mouldParent)) {
      return null;
    }

    return mouldParent.getChild((this.ordinal() % 11) * 17);
  }
}
