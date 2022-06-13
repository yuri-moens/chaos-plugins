package io.reisub.unethicalite.giantsfoundry.tasks;

import io.reisub.unethicalite.giantsfoundry.GiantsFoundry;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryHelper;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryState;
import io.reisub.unethicalite.giantsfoundry.MouldHelper;
import io.reisub.unethicalite.giantsfoundry.enums.Mould;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuAction;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.widgets.Widgets;
import net.unethicalite.client.Static;

@Slf4j
public class SetMoulds extends Task {

  private static final int MOULD_TABS_WIDGET = 47054860;
  private static final int MOULDS_WIDGET = 47054857;
  private static final int MOULD_SET_WIDGET = 47054854;
  private static final int FORTE_CHILD_INDEX = 0;
  private static final int BLADES_CHILD_INDEX = 9;
  private static final int TIPS_CHILD_INDEX = 18;
  private static final int NUMBER_OF_MOULDS = 11;
  private static final int MOULD_OFFSET = 17;

  @Inject
  private GiantsFoundry plugin;
  @Inject
  private GiantsFoundryState giantsFoundryState;
  @Inject
  private GiantsFoundryHelper giantsFoundryHelper;
  @Inject
  private MouldHelper mouldHelper;

  @Override
  public String getStatus() {
    return "Setting moulds";
  }

  @Override
  public boolean validate() {
    return giantsFoundryState.getGameStage() == 0
        && Static.getClient().getTickCount() - plugin.getLastSetMoulds() > 5
        && giantsFoundryState.getCurrentStage() == null
        && giantsFoundryState.getFirstPartCommission() != 0;
  }

  @Override
  public void execute() {
    final TileObject mj = TileObjects.getNearest("Mould jig (Empty)");

    if (mj == null) {
      return;
    }

    mj.interact("Setup");
    Time.sleepTicksUntil(() -> Widgets.isVisible(Widgets.fromId(MOULDS_WIDGET)), 20);

    final Widget tabWidget = Widgets.fromId(MOULD_TABS_WIDGET);
    final Widget mouldWidget = Widgets.fromId(MOULDS_WIDGET);
    final Widget setMouldWidget = Widgets.fromId(MOULD_SET_WIDGET);
    final Widget optionWidget = mouldWidget.getChild(0);

    final List<Mould> bestMoulds = mouldHelper.getBestMoulds();

    tabWidget.interact(1, MenuAction.CC_OP.getId(), FORTE_CHILD_INDEX, MOULD_TABS_WIDGET);
    optionWidget.interact(
        1,
        MenuAction.CC_OP.getId(),
        (bestMoulds.get(0).ordinal() % NUMBER_OF_MOULDS) * MOULD_OFFSET,
        MOULDS_WIDGET
    );

    tabWidget.interact(1, MenuAction.CC_OP.getId(), BLADES_CHILD_INDEX, MOULD_TABS_WIDGET);
    optionWidget.interact(
        1,
        MenuAction.CC_OP.getId(),
        (bestMoulds.get(1).ordinal() % NUMBER_OF_MOULDS) * MOULD_OFFSET,
        MOULDS_WIDGET
    );

    tabWidget.interact(1, MenuAction.CC_OP.getId(), TIPS_CHILD_INDEX, MOULD_TABS_WIDGET);
    optionWidget.interact(
        1,
        MenuAction.CC_OP.getId(),
        (bestMoulds.get(2).ordinal() % NUMBER_OF_MOULDS) * MOULD_OFFSET,
        MOULDS_WIDGET
    );

    setMouldWidget.interact(1, MenuAction.CC_OP.getId(), -1, MOULD_SET_WIDGET);

    plugin.setLastSetMoulds(Static.getClient().getTickCount());
  }
}