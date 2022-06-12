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
import net.runelite.api.TileObject;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.widgets.Widgets;

@Slf4j
public class SetMoulds extends Task {

  private static final int MOULD_WIDGET = 47054851;
  private static final int MOULD_TABS_WIDGET = 47054860;
  private static final int MOULD_SET_WIDGET = 47054854;
  private static final int TAB_VARBIT = 13909;

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
        && giantsFoundryState.getCurrentStage() == null
        && giantsFoundryState.getFirstPartCommission() != 0;
  }

  @Override
  public void execute() {
    TileObject mj = TileObjects.getNearest("Mould jig (Empty)");
    if (mj == null) {
      return;
    }

    mj.interact("Setup");
    Time.sleepTicksUntil(() -> Widgets.fromId(MOULD_WIDGET).isVisible(), 20);

    final Widget tabWidget = Widgets.fromId(47054860);
    final Widget mouldWidget = Widgets.fromId(47054857);
    final Widget setMouldWidget = Widgets.fromId(47054854);

    final Widget forteWidget = tabWidget.getChild(0);
    final Widget bladesWidget = tabWidget.getChild(9);
    final Widget tipsWidget = tabWidget.getChild(18);

    List<Mould> bestMoulds = mouldHelper.getBestMoulds();

    forteWidget.interact("View");
    Widget option = mouldWidget.getChild((bestMoulds.get(0).ordinal() % 11) * 17);
    option.interact("Select");

    bladesWidget.interact("View");
    option = mouldWidget.getChild((bestMoulds.get(1).ordinal() % 11) * 17);
    option.interact("Select");

    tipsWidget.interact("View");
    option = mouldWidget.getChild((bestMoulds.get(2).ordinal() % 11) * 17);
    option.interact("Select");

    setMouldWidget.interact(1, 57, -1, 47054854);
  }
}