package io.reisub.unethicalite.giantsfoundry.tasks;

import io.reisub.unethicalite.giantsfoundry.GiantsFoundry;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryState;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.NPC;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.widgets.Dialog;

public class HandIn extends Task {
  @Inject
  private GiantsFoundry plugin;

  @Inject
  private GiantsFoundryState giantsFoundryState;

  @Override
  public String getStatus() {
    return "Handing in the sword";
  }

  @Override
  public boolean validate() {
    return giantsFoundryState.getProgressAmount() == 1000;
  }

  @Override
  public void execute() {
    Time.sleepTicksUntil(() -> Players.getLocal().isIdle(), 10);
    NPC kovac = NPCs.getNearest("Kovac");
    if (kovac == null) {
      return;
    }
    kovac.interact("Hand-in");
    while (Dialog.isViewingOptions() || Dialog.canContinue()) {
      if (Dialog.isViewingOptions()) {
        List<Widget> options = Dialog.getOptions();

        for (Widget opt : options) {
          if (opt.getText().startsWith("Yes")) {
            Dialog.chooseOption(opt.getIndex());
            return;
          }
        }
      }

      if (Dialog.canContinue()) {
        Dialog.continueSpace();
      }
    }
  }
}