package io.reisub.unethicalite.giantsfoundry;


import com.google.common.collect.ImmutableList;
import io.reisub.unethicalite.giantsfoundry.enums.CommissionType;
import io.reisub.unethicalite.giantsfoundry.enums.Mould;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.game.Vars;
import net.unethicalite.api.widgets.Widgets;

public class MouldHelper {
  public static final int SELECT_MOULD_SCRIPT = 6098;
  static final int MOULD_LIST_PARENT = 47054857;
  static final int DRAW_MOULD_LIST_SCRIPT = 6093;
  static final int REDRAW_MOULD_LIST_SCRIPT = 6095;
  static final int RESET_MOULD_SCRIPT = 6108;
  private static final int SWORD_TYPE_1_VARBIT = 13907; // 4=Broad
  private static final int SWORD_TYPE_2_VARBIT = 13908; // 3=Flat
  private static final int DISABLED_TEXT_COLOR = 0x9f9f9f;
  private static final int GREEN = 0xdc10d;

  @Inject
  private Client client;

  public List<Mould> getBestMoulds() {
    final CommissionType type1 = CommissionType.forVarbit(Vars.getBit(SWORD_TYPE_1_VARBIT));
    final CommissionType type2 = CommissionType.forVarbit(Vars.getBit(SWORD_TYPE_2_VARBIT));

    return ImmutableList.of(
        getBestMould(type1, type2, Mould.getFortes()),
        getBestMould(type1, type2, Mould.getBlades()),
        getBestMould(type1, type2, Mould.getTips())
    );
  }

  private Mould getBestMould(CommissionType type1, CommissionType type2, List<Mould> moulds) {
    int bestScore = 0;
    Mould best = null;

    for (Mould mould : moulds) {
      if (!mould.isUnlocked()) {
        continue;
      }

      final int score = mould.getScore(type1, type2);

      if (score > bestScore) {
        best = mould;
        bestScore = score;
      }
    }

    return best;
  }

  public Widget getBestMould() {
    Widget parent = Widgets.fromId(MOULD_LIST_PARENT);
    if (parent == null || parent.getChildren() == null) {
      return null;
    }

    Map<Mould, Widget> mouldToChild = getOptions(parent.getChildren());

    int bestScore = -1;
    Widget bestWidget = null;
    CommissionType type1 = CommissionType.forVarbit(Vars.getBit(SWORD_TYPE_1_VARBIT));
    CommissionType type2 = CommissionType.forVarbit(Vars.getBit(SWORD_TYPE_2_VARBIT));
    for (Map.Entry<Mould, Widget> entry : mouldToChild.entrySet()) {
      Mould mould = entry.getKey();

      if (!mould.isUnlocked()) {
        continue;
      }

      int score = mould.getScore(type1, type2);
      if (score > bestScore) {
        bestScore = score;
        bestWidget = entry.getValue();
      }
    }

    return bestWidget;
  }

  private Map<Mould, Widget> getOptions(Widget[] children) {
    Map<Mould, Widget> mouldToChild = new LinkedHashMap<>();
    for (int i = 2; i < children.length; i += 17) {
      Widget child = children[i];
      Mould mould = Mould.forName(child.getText());
      if (mould != null && child.getTextColor() != DISABLED_TEXT_COLOR) {
        mouldToChild.put(mould, child);
      }
    }
    return mouldToChild;
  }
}
