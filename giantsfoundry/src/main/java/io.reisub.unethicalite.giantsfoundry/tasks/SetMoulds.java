package io.reisub.unethicalite.giantsfoundry.tasks;

import io.reisub.unethicalite.giantsfoundry.GiantsFoundry;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryHelper;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryState;
import io.reisub.unethicalite.giantsfoundry.MouldHelper;
import io.reisub.unethicalite.giantsfoundry.enums.Stage;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuAction;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.widgets.Widgets;

import javax.inject.Inject;

@Slf4j
public class SetMoulds extends Task {
    private static final int MOULD_WIDGET = 47054851;
    private static final int MOULD_TABS_WIDGET = 47054860;
    private static final int MOULD_SET_WIDGET = 47054854;
    private static final int TAB_VARBIT = 13909;

    @Inject private GiantsFoundry plugin;

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
        return false; //SET MOULDS MANUALLY
        //return giantsFoundryState.getGameStage() == 0 && giantsFoundryState.getCurrentStage() == null && giantsFoundryState.getFirstPartCommission() != 0;
    }

    @Override
    public void execute() {

        TileObject mj = TileObjects.getNearest("Mould jig (Empty)");
        if (mj == null) {
            return;
        }
        mj.interact("Setup");
        Time.sleepTicksUntil(() -> Widgets.fromId(MOULD_WIDGET).isVisible(), 20);

        for (int i = 0; i<=2; i++) {
            Widget[] wl = Widgets.fromId(MOULD_TABS_WIDGET).getChildren();
            if (wl == null) {
                return;
            }
            System.out.println(wl.length);
            Widget widdy = wl[i*9];
            if (!widdy.hasAction("View")) {
                return;
            }
            Widgets.fromId(MOULD_TABS_WIDGET).interact(
                    1,
                    57,
                    widdy.getIndex(),
                    widdy.getId());
            Time.sleepTicks(2);
            Widget bm = mouldHelper.getBestMould();
            System.out.println(bm.getId());
            bm.interact(
                    1,
                    57,
                    bm.getIndex(),
                    bm.getId());
        }

        Time.sleepTicks(2);

        Widget sw = Widgets.fromId(MOULD_SET_WIDGET);
        sw.interact(
                1,
                57,
                sw.getIndex(),
                sw.getId());
        Time.sleepTicksUntil(() -> Widgets.fromId(MOULD_WIDGET).isHidden(), 10);

    }
}