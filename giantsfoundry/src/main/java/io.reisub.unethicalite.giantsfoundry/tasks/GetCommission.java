package io.reisub.unethicalite.giantsfoundry.tasks;

import io.reisub.unethicalite.giantsfoundry.GiantsFoundry;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryState;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.NPC;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.widgets.Dialog;

@Slf4j
public class GetCommission extends Task {
  @Inject
  private GiantsFoundry plugin;

  @Inject
  private GiantsFoundryState giantsFoundryState;

  @Override
  public String getStatus() {
    return "Getting a commission";
  }

  @Override
  public boolean validate() {
    return giantsFoundryState.getCurrentStage() == null
        && giantsFoundryState.getFirstPartCommission() == 0;
  }

  @Override
  public void execute() {
    NPC kovac = NPCs.getNearest("Kovac");
    if (kovac == null) {
      return;
    }
    kovac.interact("Commission");
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